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
import oracle.odi.domain.project.finder.IOdiFolderFinder;
import oracle.odi.domain.topology.finder.IOdiContextFinder;
import oracle.odi.domain.model.finder.IOdiDataStoreFinder;
import oracle.odi.domain.topology.OdiContext;
import oracle.odi.domain.model.OdiDataStore; 
import oracle.odi.domain.project.OdiFolder;
import oracle.odi.domain.project.OdiInterface;
import oracle.odi.domain.project.interfaces.DataSet;
import oracle.odi.interfaces.interactive.support.InteractiveInterfaceHelperWithActions;
import oracle.odi.interfaces.interactive.support.actions.InterfaceActionAddSourceDataStore;
import oracle.odi.interfaces.interactive.support.actions.InterfaceActionSetTargetDataStore;
import oracle.odi.interfaces.interactive.support.actions.InterfaceActionOnTargetDataStoreComputeAutoMapping;
import oracle.odi.interfaces.interactive.support.aliascomputers.AliasComputerDoubleChecker;
import oracle.odi.interfaces.interactive.support.clauseimporters.ClauseImporterDefault;
import oracle.odi.interfaces.interactive.support.mapping.automap.AutoMappingComputerColumnName;
import oracle.odi.interfaces.interactive.support.mapping.matchpolicy.MappingMatchPolicyColumnName;
import oracle.odi.interfaces.interactive.support.targetkeychoosers.TargetKeyChooserPrimaryKey;
 
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

  /* Instance Creation */
  ITransactionDefinition txnDef = new DefaultTransactionDefinition();
  ITransactionManager tm = odiInstance.getTransactionManager();
  ITransactionStatus txnStatus = tm.getTransaction(txnDef);

  /* Interface Creation */  
  println("Starting Interface Creation...");
  OdiFolder sdkFolder = ((IOdiFolderFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiFolder.class)).findByName("SDK Folder").get(0);
  OdiContext sdkContext = ((IOdiContextFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiContext.class)).findDefaultContext();
  OdiInterface sdkInterface = new OdiInterface(sdkFolder, "Load TRG_EMP", sdkContext);
  InteractiveInterfaceHelperWithActions sdkIntHelper = new InteractiveInterfaceHelperWithActions(sdkInterface, odiInstance, odiInstance.getTransactionalEntityManager());
  OdiDataStore sdkSrcDatastore = ((IOdiDataStoreFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiDataStore.class)).findByName("SRC_EMP", "DEMO_SRC");
  DataSet dataSet = sdkInterface.getDataSets().get(0);
  sdkIntHelper.performAction(new InterfaceActionAddSourceDataStore(sdkSrcDatastore, dataSet, new AliasComputerDoubleChecker(), new ClauseImporterDefault(), new AutoMappingComputerColumnName()));

  OdiDataStore sdkTrgDatastore = ((IOdiDataStoreFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiDataStore.class)).findByName("TRG_EMP", "DEMO_TRG");
  sdkIntHelper.performAction(new InterfaceActionSetTargetDataStore(sdkTrgDatastore, new MappingMatchPolicyColumnName(), new AutoMappingComputerColumnName(), new AutoMappingComputerColumnName(), new TargetKeyChooserPrimaryKey()));
  sdkIntHelper.performAction(new InterfaceActionOnTargetDataStoreComputeAutoMapping());	
  sdkIntHelper.computeSourceSets();
  sdkIntHelper.preparePersist();
  println("Completed Interface Creation.");
  
  /* Commit Changes */
  tm.commit(txnStatus);
  /* Release Resources */
  auth.close();
  odiInstance.close();

} catch (Exception e) {
  auth.close();
  odiInstance.close();
  println(e);
}