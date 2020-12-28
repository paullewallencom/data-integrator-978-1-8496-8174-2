//Created by ODI Studio

import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.security.Authentication;
import oracle.odi.core.persistence.transaction.ITransactionDefinition;
import oracle.odi.core.persistence.transaction.support.DefaultTransactionDefinition;
import oracle.odi.core.persistence.transaction.ITransactionManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.runtime.agent.RuntimeAgent;
import oracle.odi.domain.project.finder.IOdiInterfaceFinder;
import oracle.odi.domain.project.OdiInterface;
import oracle.odi.generation.support.OdiScenarioGeneratorImpl;
import oracle.odi.generation.IOdiScenarioGenerator;
import oracle.odi.runtime.agent.invocation.ExecutionInfo;
import oracle.odi.domain.runtime.session.finder.IOdiSessionFinder;
import oracle.odi.domain.runtime.scenario.finder.IOdiScenarioFinder;
import oracle.odi.domain.runtime.session.OdiSession;
import oracle.odi.domain.runtime.scenario.OdiScenario;

try {
  /* ODI Credentials */
  String odiSupervisorUser = "SUPERVISOR";
  String odiSupervisorPassword = "SUPERVISOR"; 

  /* Master Repository Information */
  String masterRepositoryJdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl"; 
  String masterRepositoryJdbcDriver = "oracle.jdbc.OracleDriver";
  String masterRepositoryJdbcUser = "SDK_MREP";
  String masterRepositoryJdbcPassword = "SDK_MREP";

  /* Work Repository Information */
  String workRepositoryName = "SDK_WORKREP";
  
  /* OdiInstance Creation */
  MasterRepositoryDbInfo mRepDbInfo= new MasterRepositoryDbInfo(masterRepositoryJdbcUrl, masterRepositoryJdbcDriver, masterRepositoryJdbcUser, masterRepositoryJdbcPassword.toCharArray(), new PoolingAttributes()); 
  WorkRepositoryDbInfo wRepDbInfo= new WorkRepositoryDbInfo(workRepositoryName, new PoolingAttributes()); 
  OdiInstance odiInstance = OdiInstance.createInstance(new OdiInstanceConfig(mRepDbInfo, wRepDbInfo));
  Authentication auth = odiInstance.getSecurityManager().createAuthentication(odiSupervisorUser, odiSupervisorPassword.toCharArray());
  odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth); 

  println("Starting Interface Execution...");

  OdiInterface sdkInterface = ((IOdiInterfaceFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiInterface.class)).findByName("Load TRG_EMP", "ODI_SDK_PROJECT", "SDK Folder").get(0);
  if (!((IOdiScenarioFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiScenario.class)).findLatestByName("LOAD_TRG_EMP")) {
    println("Generating Scenario from Interface...");
    ITransactionDefinition txnDef = new DefaultTransactionDefinition();
    ITransactionManager tm = odiInstance.getTransactionManager();
    ITransactionStatus txnStatus = tm.getTransaction(txnDef);
    IOdiScenarioGenerator sdkIntScenario = new OdiScenarioGeneratorImpl(odiInstance);
    sdkIntScenario.generateScenario(sdkInterface, "LOAD_TRG_EMP", "001");
    tm.commit(txnStatus); 
    }
  
  println("Executing Scenario...");
  RuntimeAgent runtimeAgent = new RuntimeAgent(odiInstance, odiSupervisorUser, odiSupervisorPassword.toCharArray());
  ExecutionInfo sdkExecInfo = runtimeAgent.startScenario("LOAD_TRG_EMP", "001", null, null, "GLOBAL", 5, null, true);
  OdiSession sdkSession = ((IOdiSessionFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiSession.class)).findBySessionId(sdkExecInfo.getSessionId());
  println("Completed Interface Execution. Session " + sdkSession.getName() + " (" + sdkSession.getSessionId() + ").") ;
  println("Status: " + sdkSession.getStatus());
  println("Return Code: " + sdkSession.getReturnCode());
  String sdkError = sdkSession.getErrorMessage();
  if (sdkError != null) {
    println("Error Message: " + sdkSession.getErrorMessage());
  }
  
  /* Release Resources */
  auth.close();
  odiInstance.close();
  
} catch (Exception e) {
  auth.close();
  odiInstance.close();
  println(e);
}