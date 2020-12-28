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
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.OdiFolder;
 
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

  /* Transaction Creation */
  ITransactionDefinition txnDef = new DefaultTransactionDefinition();
  ITransactionManager tm = odiInstance.getTransactionManager();
  ITransactionStatus txnStatus = tm.getTransaction(txnDef);

  /* Project and Folder Creation */
  println("Starting Project and Folder Creation...");
  OdiProject sdkProject = new OdiProject("ODI SDK Project","ODI_SDK_PROJECT");
  OdiFolder sdkFolder = new OdiFolder(sdkProject,"SDK Folder");
  odiInstance.getTransactionalEntityManager().persist(sdkProject);
  println("Completed Project and Folder Creation.");
    
  /* Commit Changes to Repository */
  tm.commit(txnStatus);
  /* Release Resources */
  auth.close();
  odiInstance.close();

} catch (Exception e) {
  auth.close();
  odiInstance.close();
  println(e);
}