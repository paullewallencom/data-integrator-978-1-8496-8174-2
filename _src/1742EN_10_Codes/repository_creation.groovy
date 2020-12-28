//Created by ODI Studio

import oracle.odi.setup.AuthenticationConfiguration;
import oracle.odi.setup.IMasterRepositorySetup;
import oracle.odi.setup.IWorkRepositorySetup;
import oracle.odi.setup.TechnologyName;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.setup.JdbcProperties;
import oracle.odi.core.repository.WorkRepository;
import oracle.odi.setup.support.MasterRepositorySetupImpl;
import oracle.odi.setup.support.WorkRepositorySetupImpl;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.security.Authentication;

try {
  /* ODI Credentials */
  String odiSupervisorUser = "SUPERVISOR";
  String odiSupervisorPassword = "SUPERVISOR"; 
  
  /* Repository DB Credentials */
  String dbaUser = "system";
  char[] dbaPwd = "oracle";

  /* Master Repository Information */
  TechnologyName masterRepositoryTechnology = TechnologyName.ORACLE;
  String masterRepositoryJdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl"; 
  String masterRepositoryJdbcDriver = "oracle.jdbc.OracleDriver";
  String masterRepositoryJdbcUser = "SDK_MREP";
  String masterRepositoryJdbcPassword = "SDK_MREP";
  int masterRepositoryId = 0;

  /* Work Repository Information */
  TechnologyName workRepositoryTechnology = TechnologyName.ORACLE;
  String workRepositoryJdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl"; 
  String workRepositoryJdbcDriver = "oracle.jdbc.OracleDriver"; 
  String workRepositoryJdbcUser = "SDK_WREP"; 
  String workRepositoryJdbcPassword = "SDK_WREP"; 
  String workRepositoryName = "SDK_WORKREP";
  WorkRepository.WorkType wRepType = WorkRepository.WorkType.valueOf("DESIGN");
  int workRepositoryId = 1;
  
  /* Master Repository Creation */
  println("Starting Master Repository Creation...");
  IMasterRepositorySetup masterRepositorySetup = new MasterRepositorySetupImpl();
  AuthenticationConfiguration authConf = AuthenticationConfiguration.createStandaloneAuthenticationConfiguration(odiSupervisorPassword.toCharArray());
  JdbcProperties mRepJdbcInfo = new JdbcProperties(masterRepositoryJdbcUrl, masterRepositoryJdbcDriver, masterRepositoryJdbcUser, masterRepositoryJdbcPassword.toCharArray());
  masterRepositorySetup.createMasterRepository(mRepJdbcInfo, dbaUser, dbaPwd, masterRepositoryId, masterRepositoryTechnology, true, authConf, null);
  println("Master Repository Creation Successful.");
  
  /* OdiInstance Creation */
  MasterRepositoryDbInfo mRepDbInfo= new MasterRepositoryDbInfo(masterRepositoryJdbcUrl, masterRepositoryJdbcDriver, masterRepositoryJdbcUser, masterRepositoryJdbcPassword.toCharArray(), new PoolingAttributes()); 
  OdiInstance odiInstance = OdiInstance.createInstance(new OdiInstanceConfig(mRepDbInfo, null));
  Authentication auth = odiInstance.getSecurityManager().createAuthentication(odiSupervisorUser, odiSupervisorPassword.toCharArray());
  odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth);
 
  /* Work Repository Creation */
  println("Starting Work Repository Creation...");
  IWorkRepositorySetup workRepositorySetup = new WorkRepositorySetupImpl(odiInstance);
  JdbcProperties wRepJdbcInfo = new JdbcProperties(workRepositoryJdbcUrl, workRepositoryJdbcDriver, workRepositoryJdbcUser, workRepositoryJdbcPassword);
  workRepositorySetup.createWorkRepository(wRepType, wRepJdbcInfo, workRepositoryId, workRepositoryName, workRepositoryTechnology, true, null);
  println("Work Repository Creation Successful.");
  
  /* Release Resources */
  auth.close();
  odiInstance.close();
}
catch (Exception e)
{
  auth.close();
  odiInstance.close();
  println(e);
}