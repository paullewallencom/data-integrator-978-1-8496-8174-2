import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.io.File;

import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.domain.project.OdiInterface;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiInterfaceFinder;
import oracle.odi.domain.project.finder.IOdiProjectFinder;
import oracle.odi.impexp.EncodingOptions;
import oracle.odi.impexp.smartie.ISmartExportService;
import oracle.odi.impexp.smartie.ISmartExportable;
import oracle.odi.impexp.smartie.impl.SmartExportServiceImpl;
import oracle.odi.publicapi.samples.SimpleOdiInstanceHandle;

public class smartExport
{


	public static void main(String[] args) {
		//
		// Command Line Arguments are required for WorkRepo Name, Work Repo User, Work Repo Password,Export Destination Folder, Project Code, Interface Name in this exact order
		// For example java smartExport WORKREP1 SUPERVISOR SUNOPSIS c:\odi_exports\smartExport.xml Cookbook_Chappter_11 LoadEmployees
		
		// Loop through command line arguments
		     System.out.println("The following command line arguments were passed:");      
	   for (int i=0; i < args.length; i++){
		System.out.println("arg[" + i + "]: " + args[i]);
	   }
		// Connect to the ODI Master and Work Repositories from where data will be exported
		String srcMasterRepoUrl = "jdbc:oracle:thin:@localhost:1521:xe"; //JDBC URL String
		String srcMasterRepoDriver = "oracle.jdbc.OracleDriver"; //JDBC Driver String
		String srcMasterRepoDatabaseUser = "ODI_MASTER"; //Master Repo Database User
		String srcMasterRepoDatabasePassw = "oracle1"; //Master Repo Database Password
		String srcWorkRepoName = args[0].toString(); // Passed as Argument 1 on command line
		String srcOdiSupervisor =  args[1].toString();  // Passed as Argument 2 on command line
		String srcOdiSupervisorPassw =  args[2].toString();  // Passed as Argument 3 on command line
		
		final SimpleOdiInstanceHandle odiInstanceHandle = SimpleOdiInstanceHandle.create (srcMasterRepoUrl, srcMasterRepoDriver, srcMasterRepoDatabaseUser, srcMasterRepoDatabasePassw, srcWorkRepoName, srcOdiSupervisor, srcOdiSupervisorPassw);
		final LinkedList<String> relatedObjectsTags = new LinkedList<String> ();
		relatedObjectsTags.add ("");

		//
		// Set the location, and name of the Smart Export .xml file (the file which will be created during the Smart Export operation, and which will hold the exported objects)
		final String folderPath = args[3].toString(); //Passed as Argument 4 on command line
		final String smartExportFileName = "smartExport.xml";
		
		// Set the encoding of XML file
		final EncodingOptions expeo = new EncodingOptions ("1.0", "ISO8859_9",  "ISO-8859-9");
		
		Locale locale = new Locale ("en", "US");
		Locale.setDefault (locale);

		//
		// From which Project will be exported the Integration Interfaces? Set below the Project code
		final String srcOdiProjectCode = args[4].toString(); //Passed as Argument5 on command line
		final String srcOdiInterfaceName = args[5].toString();//Passed as Argument6 on command line
		//
		// Export the Integration Interface from the Project
		final List<ISmartExportable> expIntegrationInterfaces = new LinkedList<ISmartExportable> ();		

		TransactionTemplate transaction = new TransactionTemplate (odiInstanceHandle.getOdiInstance().getTransactionManager());
		
		transaction.execute (new ITransactionCallback ()
			{
				public Object doInTransaction(ITransactionStatus pStatus)
				{
				//	OdiProject project = ((IOdiProjectFinder) odiInstanceHandle.getOdiInstance ().getTransactionalEntityManager ().getFinder( OdiProject.class)).findByCode (srcOdiProjectCode); //$NON-NLS-1$

					Collection<OdiInterface> odiInterfaces = ((IOdiInterfaceFinder)odiInstanceHandle.getOdiInstance().getTransactionalEntityManager().getFinder(OdiInterface.class)).findByName(srcOdiInterfaceName, srcOdiProjectCode); //$NON-NLS-1$
							
					for (OdiInterface pop : odiInterfaces) 
					{
						expIntegrationInterfaces.add( (ISmartExportable) pop); 
						System.out.println("FOUND : "+ pop.getName ());
					}
							
					ISmartExportService esvc = new SmartExportServiceImpl (odiInstanceHandle.getOdiInstance ());
					try {
						esvc.exportToXml (expIntegrationInterfaces, folderPath, smartExportFileName, true, false,expeo, false, relatedObjectsTags);
					} catch (IOException e) {
						e.printStackTrace ();
					}
		
					return null;
				} // doInTransaction
			} 
		); // transaction.execute
	} // main
}
