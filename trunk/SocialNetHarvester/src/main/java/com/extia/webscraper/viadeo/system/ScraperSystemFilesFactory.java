package com.extia.webscraper.viadeo.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.extia.webscraper.exception.ScrapperException;

public class ScraperSystemFilesFactory {
	
	private File resultFile;
	private File keyWordsReportFile;
	private ScrappingSettings scrappingSettings;
	
	
	private ScrappingSettings getScrappingSettings() {
		return scrappingSettings;
	}

	public void setScrappingSettings(ScrappingSettings scrappingSettings) {
		this.scrappingSettings = scrappingSettings;
	}

	public File getKeyWordsReportFile() throws IOException, ScrapperException{
		if(keyWordsReportFile == null){
			keyWordsReportFile = createAndBackup(getScrappingSettings().getKeyWordsReportFilePath());
		}
		return keyWordsReportFile;
	}

	public File getResultFile() throws IOException, ScrapperException {
		if(resultFile == null){
			resultFile = createAndBackup(getScrappingSettings().getResultFilePath());
		}
		return resultFile;
	}

	public String getResultFilePath() throws IOException, ScrapperException{
		return getResultFile().getAbsolutePath();
	}
	
	private File getWortkingDir() throws ScrapperException{
		return createAndeBackupDir(new File(getScrappingSettings().getWorkingDirPath()));
	}
	
	private File getBackupDir() throws ScrapperException{
		return createAndeBackupDir(new File(getWortkingDir(), "backup"));
	}
	
	public File getHistoryDir() throws ScrapperException{
		return createAndeBackupDir(new File(getWortkingDir(), "history"));
	}
	
	private File createAndeBackupDir(File dir) throws ScrapperException{
		if(!(dir.exists() || dir.mkdir())){
			throw new ScrapperException("Le répertoire " + dir.getAbsolutePath() +" n'existe pas et ne peut être créé.");
		}
		return dir;
	}

	private File createAndBackup(String filePath) throws IOException, ScrapperException{
		File result = null;
		if(filePath != null){
			File backupDir = getBackupDir();
			if(!backupDir.exists() || !backupDir.isDirectory()){
				throw new ScrapperException("Le répertoire " + backupDir.getAbsolutePath() +" n'existe pas. Veuillez le créer.");
			}else{
				result = new File(filePath);

				if(!result.exists()){
					result.createNewFile();
				}else{
					int indexResultFile = 1;
					File backUpFile = new File(backupDir, "backup_" + result.getName());
					while(backUpFile.exists()){
						backUpFile = new File(backupDir, "backup_" + indexResultFile++ + result.getName());
					}
					backUpFile.createNewFile();
					Files.copy(Paths.get(result.getAbsolutePath()), new FileOutputStream(backUpFile));
				}

			}
		}
		return result;
	}
}
