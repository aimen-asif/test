alter table Attachment drop constraint FK_7ndpfa311i50bq7hy18q05va3;
alter table Attachment drop constraint FK_hqupx569krp0f0sgu9kh87513;
alter table BooleanExpression drop constraint FK_394nf2qoc0k9ok6omgd6jtpso;
alter table CorrelationPropertyInfo drop constraint FK_hrmx1m882cejwj9c04ixh50i4;
alter table Deadline drop constraint FK_68w742sge00vco2cq3jhbvmgx;
alter table Deadline drop constraint FK_euoohoelbqvv94d8a8rcg8s5n;
alter table Delegation_delegates drop constraint FK_gn7ula51sk55wj1o1m57guqxb;
alter table Delegation_delegates drop constraint FK_fajq6kossbsqwr3opkrctxei3;
alter table ErrorInfo drop constraint FK_cms0met37ggfw5p5gci3otaq0;
alter table Escalation drop constraint FK_ay2gd4fvl9yaapviyxudwuvfg;
alter table EventTypes drop constraint FK_nrecj4617iwxlc65ij6m7lsl1;
alter table I18NText drop constraint FK_k16jpgrh67ti9uedf6konsu1p;
alter table I18NText drop constraint FK_fd9uk6hemv2dx1ojovo7ms3vp;
alter table I18NText drop constraint FK_4eyfp69ucrron2hr7qx4np2fp;
alter table I18NText drop constraint FK_pqarjvvnwfjpeyb87yd7m0bfi;
alter table I18NText drop constraint FK_o84rkh69r47ti8uv4eyj7bmo2;
alter table I18NText drop constraint FK_g1trxri8w64enudw2t1qahhk5;
alter table I18NText drop constraint FK_qoce92c70adem3ccb3i7lec8x;
alter table I18NText drop constraint FK_bw8vmpekejxt1ei2ge26gdsry;
alter table I18NText drop constraint FK_21qvifarxsvuxeaw5sxwh473w;
alter table Notification drop constraint FK_bdbeml3768go5im41cgfpyso9;
alter table Notification_BAs drop constraint FK_mfbsnbrhth4rjhqc2ud338s4i;
alter table Notification_BAs drop constraint FK_fc0uuy76t2bvxaxqysoo8xts7;
alter table Notification_email_header drop constraint FK_ptaka5kost68h7l3wflv7w6y8;
alter table Notification_email_header drop constraint FK_eth4nvxn21fk1vnju85vkjrai;
alter table Notification_Recipients drop constraint FK_blf9jsrumtrthdaqnpwxt25eu;
alter table Notification_Recipients drop constraint FK_3l244pj8sh78vtn9imaymrg47;
alter table PeopleAssignments_BAs drop constraint FK_t38xbkrq6cppifnxequhvjsl2;
alter table PeopleAssignments_BAs drop constraint FK_omjg5qh7uv8e9bolbaq7hv6oh;
alter table PeopleAssignments_ExclOwners drop constraint FK_pth28a73rj6bxtlfc69kmqo0a;
alter table PeopleAssignments_ExclOwners drop constraint FK_b8owuxfrdng050ugpk0pdowa7;
alter table PeopleAssignments_PotOwners drop constraint FK_tee3ftir7xs6eo3fdvi3xw026;
alter table PeopleAssignments_PotOwners drop constraint FK_4dv2oji7pr35ru0w45trix02x;
alter table PeopleAssignments_Recipients drop constraint FK_4g7y3wx6gnokf6vycgpxs83d6;
alter table PeopleAssignments_Recipients drop constraint FK_enhk831fghf6akjilfn58okl4;
alter table PeopleAssignments_Stakeholders drop constraint FK_met63inaep6cq4ofb3nnxi4tm;
alter table PeopleAssignments_Stakeholders drop constraint FK_4bh3ay74x6ql9usunubttfdf1;
alter table Reassignment drop constraint FK_pnpeue9hs6kx2ep0sp16b6kfd;
alter table Reassignment_potentialOwners drop constraint FK_8frl6la7tgparlnukhp8xmody;
alter table Reassignment_potentialOwners drop constraint FK_qbega5ncu6b9yigwlw55aeijn;
alter table Task drop constraint FK_dpk0f9ucm14c78bsxthh7h8yh;
alter table Task drop constraint FK_nh9nnt47f3l61qjlyedqt05rf;
alter table Task drop constraint FK_k02og0u71obf1uxgcdjx9rcjc;
alter table task_comment drop constraint FK_aax378yjnsmw9kb9vsu994jjv;
alter table task_comment drop constraint FK_1ws9jdmhtey6mxu7jb0r0ufvs;
drop table Attachment;
drop table AuditTaskImpl;
drop table BAMTaskSummary;
drop table BooleanExpression;
drop table CaseFileDataLog;
drop table CaseIdInfo;
drop table CaseRoleAssignmentLog;
drop table Content;
drop table ContextMappingInfo;
drop table TimerMappingInfo;
drop table CorrelationKeyInfo;
drop table CorrelationPropertyInfo;
drop table Deadline;
drop table Delegation_delegates;
drop table DeploymentStore;
drop table email_header;
drop table ErrorInfo;
drop table Escalation;
drop table EventTypes;
drop table ExecutionErrorInfo;
drop table I18NText;
drop table NodeInstanceLog;
drop table Notification;
drop table Notification_BAs;
drop table Notification_email_header;
drop table Notification_Recipients;
drop table OrganizationalEntity;
drop table PeopleAssignments_BAs;
drop table PeopleAssignments_ExclOwners;
drop table PeopleAssignments_PotOwners;
drop table PeopleAssignments_Recipients;
drop table PeopleAssignments_Stakeholders;
drop table ProcessInstanceInfo;
drop table ProcessInstanceLog;
drop table QueryDefinitionStore;
drop table Reassignment;
drop table Reassignment_potentialOwners;
drop table RequestInfo;
drop table SessionInfo;
drop table Task;
drop table task_comment;
drop table TaskDef;
drop table TaskEvent;
drop table TaskVariableImpl;
drop table VariableInstanceLog;
drop table WorkItemInfo;
