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
import oracle.odi.domain.topology.finder.IOdiLogicalSchemaFinder;
import oracle.odi.domain.topology.finder.IOdiContextFinder;
import oracle.odi.domain.topology.OdiLogicalSchema;
import oracle.odi.domain.topology.OdiContext;
import oracle.odi.domain.model.OdiModel;
import oracle.odi.domain.model.OdiDataStore; 
import oracle.odi.domain.model.OdiColumn; 
import oracle.odi.domain.model.OdiKey; 
 
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
  
  /* Models Creation */
  println("Starting Source Model Creation...");
  OdiLogicalSchema srcLogicalSchema = ((IOdiLogicalSchemaFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiLogicalSchema.class)).findByName("DEMO_SRC");
  OdiContext sdkContext = ((IOdiContextFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiContext.class)).findDefaultContext();
  OdiModel srcModel = new OdiModel(srcLogicalSchema, "DEMO_SRC", "DEMO_SRC");
  srcModel.setReverseContext(sdkContext);
  odiInstance.getTransactionalEntityManager().persist(srcModel);
  println("Completed Source Model Creation.");
  
  println("Starting Target Model Creation...");
  OdiLogicalSchema trgLogicalSchema = ((IOdiLogicalSchemaFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiLogicalSchema.class)).findByName("DEMO_TRG");
  OdiModel trgModel = new OdiModel(trgLogicalSchema, "DEMO_TRG", "DEMO_TRG");
  trgModel.setReverseContext(sdkContext);
  odiInstance.getTransactionalEntityManager().persist(trgModel);
  println("Completed Target Model Creation.");
  
  /* Source Datastore Creation */
  println("Starting Source Datastore Creation...");
  OdiDataStore srcDatastore = new OdiDataStore(srcModel, "SRC_EMP");
  OdiColumn srcCol = new OdiColumn(srcDatastore, "EMPNO");
  srcCol.setDataTypeCode("NUMBER");
  srcCol.setMandatory(true);
  srcCol.setLength(4);
  srcCol.setScale(0);
  
  srcCol = new OdiColumn(srcDatastore, "ENAME");
  srcCol.setDataTypeCode("VARCHAR2");
  srcCol.setLength(10);
  srcCol.setScale(0);
  
  srcCol = new OdiColumn(srcDatastore, "JOB");
  srcCol.setDataTypeCode("VARCHAR2");
  srcCol.setLength(9);
  srcCol.setScale(0);
  
  srcCol = new OdiColumn(srcDatastore, "MGR");
  srcCol.setDataTypeCode("NUMBER");
  srcCol.setLength(4);
  srcCol.setScale(0);
  
  srcCol = new OdiColumn(srcDatastore, "HIREDATE");
  srcCol.setDataTypeCode("DATE");

  srcCol = new OdiColumn(srcDatastore, "SAL");
  srcCol.setDataTypeCode("NUMBER");
  srcCol.setLength(7);
  srcCol.setScale(2);  
  
  srcCol = new OdiColumn(srcDatastore, "COMM");
  srcCol.setDataTypeCode("NUMBER");
  srcCol.setLength(7);
  srcCol.setScale(2);   
  
  srcCol = new OdiColumn(srcDatastore, "DEPTNO");
  srcCol.setDataTypeCode("NUMBER");
  srcCol.setLength(2);
  srcCol.setScale(0); 
  println("Completed Source Datastore Creation.");
  
  /* Target Datastore Creation */
  println("Starting Target Datastore Creation...");
  OdiDataStore trgDatastore = new OdiDataStore(trgModel, "TRG_EMP");
  OdiColumn trgCol = new OdiColumn(trgDatastore, "EMPNO");
  trgCol.setDataTypeCode("NUMBER");
  trgCol.setMandatory(true);
  trgCol.setLength(4);
  trgCol.setScale(0);
  OdiKey sdkPrimaryKey = new OdiKey(trgDatastore, "PK_" + trgDatastore.getName());
  OdiKey.KeyType keyType = OdiKey.KeyType.valueOf("PRIMARY_KEY");
  sdkPrimaryKey.setKeyType(keyType);
  sdkPrimaryKey.addColumn(trgCol);
  
  trgCol = new OdiColumn(trgDatastore, "ENAME");
  trgCol.setDataTypeCode("VARCHAR2");
  trgCol.setLength(10);
  trgCol.setScale(0);
  
  trgCol = new OdiColumn(trgDatastore, "JOB");
  trgCol.setDataTypeCode("VARCHAR2");
  trgCol.setLength(9);
  trgCol.setScale(0);
  
  trgCol = new OdiColumn(trgDatastore, "MGR");
  trgCol.setDataTypeCode("NUMBER");
  trgCol.setLength(4);
  trgCol.setScale(0);
  
  trgCol = new OdiColumn(trgDatastore, "HIREDATE");
  trgCol.setDataTypeCode("DATE");

  trgCol = new OdiColumn(trgDatastore, "SAL");
  trgCol.setDataTypeCode("NUMBER");
  trgCol.setLength(7);
  trgCol.setScale(2);  
  
  trgCol = new OdiColumn(trgDatastore, "COMM");
  trgCol.setDataTypeCode("NUMBER");
  trgCol.setLength(7);
  trgCol.setScale(2);   
  
  trgCol = new OdiColumn(trgDatastore, "DEPTNO");
  trgCol.setDataTypeCode("NUMBER");
  trgCol.setLength(2);
  trgCol.setScale(0); 
  
  println("Completed Target Datastore Creation.");
  
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