package com.extia.webscraper.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.extia.webscraper.exception.ScraperException;

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

	public File getKeyWordsReportFile() throws IOException, ScraperException{
		if(keyWordsReportFile == null){
			keyWordsReportFile = createAndBackup(getScrappingSettings().getKeyWordsReportFilePath());
		}
		return keyWordsReportFile;
	}

	public File getResultFile() throws IOException, ScraperException {
		if(resultFile == null){
			resultFile = createAndBackup(getScrappingSettings().getResultFilePath());
		}
		return resultFile;
	}

	public String getResultFilePath() throws IOException, ScraperException{
		return getResultFile().getAbsolutePath();
	}
	
	private File getWortkingDir() throws ScraperException{
		return createAndeBackupDir(new File(getScrappingSettings().getWorkingDirPath()));
	}
	
	private File getBackupDir() throws ScraperException{
		return createAndeBackupDir(new File(getWortkingDir(), "backup"));
	}
	
	public File getHistoryDir() throws ScraperException{
		return createAndeBackupDir(new File(getWortkingDir(), "history"));
	}
	
	private File createAndeBackupDir(File dir) throws ScraperException{
		if(!(dir.exists() || dir.mkdir())){
			throw new ScraperException("Le répertoire " + dir.getAbsolutePath() +" n'existe pas et ne peut être créé.");
		}
		return dir;
	}

	private File createAndBackup(String filePath) throws IOException, ScraperException{
		File result = null;
		if(filePath != null){
			File backupDir = getBackupDir();
			if(!backupDir.exists() || !backupDir.isDirectory()){
				throw new ScraperException("Le répertoire " + backupDir.getAbsolutePath() +" n'existe pas. Veuillez le créer.");
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
