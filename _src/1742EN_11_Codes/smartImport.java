import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.io.File;

import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;

import oracle.odi.publicapi.samples.SimpleOdiInstanceHandle;

import oracle.odi.impexp.EncodingOptions;

import oracle.odi.impexp.smartie.ISmartImportService;
import oracle.odi.impexp.smartie.OdiSmartImportException;
import oracle.odi.impexp.smartie.impl.SmartImportServiceImpl;
import oracle.odi.impexp.smartie.impl.SmartImportFileSupport;

public class smartImport
{


	public static void main(String[] args)
	{

		//
		// Command Line Arguments are required for WorkRepo Name, Work Repo User, Work Repo Password,Export Destination Folder, Project Code, Interface Name in this exact order
		// For example java smartImport WORKREP1 SUPERVISOR SUNOPSIS c:\chap11Export.xml

		// Loop through command line arguments
		System.out.println("The following command line arguments were passed:");
		for (int i = 0; i < args.length; i++)
		{
			System.out.println("arg[" + i + "]: " + args[i]);
		}
		Locale locale = new Locale("en", "US");
		Locale.setDefault(locale);

		final String folderPath = args[3].toString();
		final String fname = "smartExport.xml";
		final String fnameAndPath = folderPath + File.separator + fname;

		final EncodingOptions expeo = new EncodingOptions("1.0", "ISO8859_9", "ISO-8859-9");

		//
		// Connect to the ODI Master and Work Repositories into which data will be imported
		String trgMasterRepoUrl = "jdbc:oracle:thin:@localhost:1521:xe";
		String trgMasterRepoDriver = "oracle.jdbc.OracleDriver";
		String trgMasterRepoDatabaseUser = "ODI_MASTER";
		String trgMasterRepoDatabasePassw = "oracle1";
		String trgWorkRepoName = args[0].toString();
		String trgOdiSupervisor = args[1].toString();
		String trgOdiSupervisorPassw = args[2].toString();

		final SimpleOdiInstanceHandle odiInstanceHandle2 = SimpleOdiInstanceHandle.create(trgMasterRepoUrl, trgMasterRepoDriver, trgMasterRepoDatabaseUser, trgMasterRepoDatabasePassw, trgWorkRepoName, trgOdiSupervisor, trgOdiSupervisorPassw);

		TransactionTemplate transaction = new TransactionTemplate(odiInstanceHandle2.getOdiInstance().getTransactionManager());

		transaction.execute(new ITransactionCallback()
		{
			public Object doInTransaction(ITransactionStatus pStatus)
			{
				boolean flag = false;

				ISmartImportService smartImpServ = new SmartImportServiceImpl(odiInstanceHandle2.getOdiInstance());

				try
				{

					SmartImportFileSupport sifs = SmartImportFileSupport.getInstance();

					flag = sifs.isSmartExportFile(fnameAndPath);
					System.out.println("\n[I N F O] isSmartExportFile returns " + flag + "\n");

					// If .xml file exists, then run the Smart Import from XML file
					smartImpServ.importFromXml(fnameAndPath);

				}
				catch (IOException e)
				{
					e.printStackTrace();

				}
				catch (OdiSmartImportException e)
				{
					e.printStackTrace();
				}

				return null;
			} // doInTransaction 
		}
	); // transaction.execute 
	} // main
}
