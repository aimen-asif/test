/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.test.functional.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.assertj.core.api.Assertions;
import org.jbpm.executor.ExecutorServiceFactory;
import org.jbpm.process.core.async.AsyncSignalEventCommand;
import org.jbpm.test.JbpmTestCase;
import org.jbpm.test.functional.event.MyFact;
import org.jbpm.test.wih.FirstErrorWorkItemHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutorService;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

/**
 * process1: start -> catch signal -> first time exception -> end
 * AsyncSignalEventCommand should be repeated when fails
 */
public class AsyncIntermediateCatchSignalTest extends JbpmTestCase {

    private static final String PROCESS_AICS = "org.jbpm.test.functional.async.AsyncIntermediateCatchSignal";
    private static final String BPMN_AICS = "org/jbpm/test/functional/async/AsyncIntermediateCatchSignal.bpmn2";

    private static final String CONDITION_CATCH = "org/jbpm/test/functional/async/ConditionEventGatewayTest.bpmn2";
    private static final String CONDITION_CATCH_ID = "org.jbpm.test.functional.async.eventgatewaytest";

    private ExecutorService executorService;
    private CountDownLatch latch;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        executorService = ExecutorServiceFactory.newExecutorService(getEmf());
        executorService.setInterval(0);
        executorService.setThreadPoolSize(3);
        addEnvironmentEntry("ExecutorService", executorService);
        addWorkItemHandler("SyncError", new FirstErrorWorkItemHandler());
        addProcessEventListener(new DefaultProcessEventListener() {
            @Override
            public void afterProcessCompleted(ProcessCompletedEvent event) {
                latch.countDown();
            }
        });
        executorService.init();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();

        executorService.destroy();
    }

    @Test(timeout = 10000)
    public void testCorrectProcessStateAfterExceptionSignalCommand() throws InterruptedException {
        latch = new CountDownLatch(1);
        RuntimeManager runtimeManager = createRuntimeManager(BPMN_AICS);
        KieSession ksession = getRuntimeEngine().getKieSession();
        ProcessInstance pi = ksession.startProcess(PROCESS_AICS);
        long pid = pi.getId();

        CommandContext ctx = new CommandContext();
        ctx.setData("DeploymentId", runtimeManager.getIdentifier());
        ctx.setData("ProcessInstanceId", pid);
        ctx.setData("Signal", "MySignal");
        ctx.setData("Event", null);

        executorService.scheduleRequest(AsyncSignalEventCommand.class.getName(), ctx);

        latch.await();
    }

    
    @Test(timeout = 20000)
    public void testCorrectProcessStateAfterExceptionSignalCommandMulti() throws InterruptedException {
        latch = new CountDownLatch(5);
        RuntimeManager runtimeManager = createRuntimeManager(BPMN_AICS);
        KieSession ksession = getRuntimeEngine().getKieSession();
        long[] pid = new long[5];
        for (int i = 0; i < 5; i++) {
            ProcessInstance pi = ksession.startProcess(PROCESS_AICS);
            pid[i] = pi.getId();

            CommandContext ctx = new CommandContext();
            ctx.setData("DeploymentId", runtimeManager.getIdentifier());
            ctx.setData("ProcessInstanceId", pi.getId());
            ctx.setData("Signal", "MySignal");
            ctx.setData("Event", null);

            executorService.scheduleRequest(AsyncSignalEventCommand.class.getName(), ctx);
        }

        latch.await();

        for (long p : pid) {
            ProcessInstance pi = ksession.getProcessInstance(p);
            Assertions.assertThat(pi).isNull();
        }
    }

    @Test(timeout = 10000, expected = org.jbpm.workflow.instance.WorkflowRuntimeException.class)
    public void testSyncGlobalSignal() {
        KieSession ksession = createKSession(BPMN_AICS);
        ksession.startProcess(PROCESS_AICS);
        ksession.signalEvent("MySignal", null);
    }

    @Test(timeout = 20000)
    public void testCondition() throws Exception{
        int count = 100;
        latch = new CountDownLatch(count);

        List<Long> pids = new ArrayList<>();
        RuntimeManager rm = createRuntimeManager(Strategy.PROCESS_INSTANCE, (String) null, CONDITION_CATCH);

        for (int i = 0; i < count; i++) {
             RuntimeEngine engine = getRuntimeEngine(ProcessInstanceIdContext.get());
             pids.add(engine.getKieSession().startProcess(CONDITION_CATCH_ID).getId());
             rm.disposeRuntimeEngine(engine);
        }

        MyFact myFact = new MyFact();
        myFact.setConditionA(true);
        rm.signalEvent("signalA", myFact);

        latch.await();
        for (long p : pids) {
            RuntimeEngine engine = rm.getRuntimeEngine(ProcessInstanceIdContext.get(p));
            try {
                engine.getKieSession();
                Assert.fail();
            } catch(org.kie.internal.runtime.manager.SessionNotFoundException e) {
                // do nothing this is correct
            } finally {
                rm.disposeRuntimeEngine(engine);
            }
        }

    }

}
