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

package org.jbpm.executor.commands.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.drools.persistence.api.TransactionManager;
import org.drools.persistence.api.TransactionManagerFactory;
import org.jbpm.runtime.manager.impl.jpa.ExecutionErrorInfo;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command that will auto acknowledge process instance errors 
 * based on the last initial node instance when the error happened.
 * 
 * Following parameters are supported by this command:
 * <ul>
 *  <li>EmfName - name of entity manager factory to be used for queries (valid persistence unit name)</li>
 *  <li>SingleRun - indicates if execution should be single run only (true|false)</li>
 *  <li>NextRun - provides next execution time (valid time expression e.g. 1d, 5h, etc)</li>
 * </ul>
 */
public class ProcessAutoAckErrorCommand extends AutoAckErrorCommand {

    private static final Logger logger = LoggerFactory.getLogger(ProcessAutoAckErrorCommand.class);

    private static final String RULE = "Process instances that previously failed but now are in different nodes - meaning node where they were was already completed - or completed/aborted";
    

    @Override
    protected List<ExecutionErrorInfo> findErrorsToAck(EntityManager em) {
        List<ExecutionErrorInfo> errorsToAck = new ArrayList<>();

        TransactionManager txm = TransactionManagerFactory.get().newTransactionManager();
        boolean txOwner = txm.begin();
        try {
            String findProcessErrorsQuery = "select error from ExecutionErrorInfo error where error.acknowledged =:acknowledged " +
                                            "and error.processInstanceId in (select pil.id from ProcessInstanceLog pil where status in (:status))";

            List<ExecutionErrorInfo> processErrorsToAck = em.createQuery(findProcessErrorsQuery, ExecutionErrorInfo.class)
                                                            .setParameter("acknowledged", new Short("0"))
                                                            .setParameter("status", Arrays.asList(ProcessInstance.STATE_COMPLETED, ProcessInstance.STATE_ABORTED))
                                                            .getResultList();
            errorsToAck.addAll(processErrorsToAck);

            String findNodeErrorsQuery = "select error from ExecutionErrorInfo error where error.acknowledged =:acknowledged " +
                                         "and CAST(error.initActivityId AS string) in (select nil.nodeInstanceId from NodeInstanceLog nil where nil.processInstanceId = error.processInstanceId and nil.nodeInstanceId = CAST(error.initActivityId AS string) and nil.type = 1)";

            List<ExecutionErrorInfo> nodeErrorsToAck = em.createQuery(findNodeErrorsQuery, ExecutionErrorInfo.class)
                                                         .setParameter("acknowledged", new Short("0"))
                                                         .getResultList();
            errorsToAck.addAll(nodeErrorsToAck);
            txm.commit(txOwner);
        } catch (Exception e) {
            logger.error("Execution error in command ProcessAutoAckErrorCommand", e);
            txm.rollback(txOwner);
        }
        return errorsToAck;

    }

    @Override
    protected String getAckRule() {
        return RULE;
    }

}
