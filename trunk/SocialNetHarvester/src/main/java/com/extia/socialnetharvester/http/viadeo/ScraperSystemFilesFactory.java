package com.extia.socialnetharvester.http.viadeo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import com.extia.socialnetharvester.ScraperException;

public class ScraperSystemFilesFactory {
	
	private File resultFile;
	private File keyWordsReportFile;
	
	@Resource(name="userSettings")
	private ViadeoUserSettings userSettings;
	
	private ViadeoUserSettings getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(ViadeoUserSettings userSettings) {
		this.userSettings = userSettings;
	}

	public File getKeyWordsReportFile() throws IOException, ScraperException{
		if(keyWordsReportFile == null){
			keyWordsReportFile = createAndBackup(getUserSettings().getKeyWordsReportFilePath());
		}
		return keyWordsReportFile;
	}

	public File getResultFile() throws IOException, ScraperException {
		if(resultFile == null){
			resultFile = createAndBackup(getUserSettings().getResultFilePath());
		}
		return resultFile;
	}

	public String getResultFilePath() throws IOException, ScraperException{
		return getResultFile().getAbsolutePath();
	}
	
	private File getWortkingDir() throws ScraperException{
		return createAndeBackupDir(new File(getUserSettings().getWorkingDirPath()));
	}
	
	private File getBackupDir() throws ScraperException{
		return createAndeBackupDir(new File(getWortkingDir(), "backup"));
	}
	
	public File getHistoryDir() throws ScraperException{
		return createAndeBackupDir(new File(getWortkingDir(), "history"));
	}
	
	private File createAndeBackupDir(File dir) throws ScraperException{
		if(!(dir.exists() || dir.mkdir())){
			throw new ScraperException("Le r�pertoire " + dir.getAbsolutePath() +" n'existe pas et ne peut �tre cr��.");
		}
		return dir;
	}

	private File createAndBackup(String filePath) throws IOException, ScraperException{
		File result = null;
		if(filePath != null){
			File backupDir = getBackupDir();
			if(!backupDir.exists() || !backupDir.isDirectory()){
				throw new ScraperException("Le r�pertoire " + backupDir.getAbsolutePath() +" n'existe pas. Veuillez le cr�er.");
			}else{
				result = new File(filePath);

				if(result.exists()){
					int indexResultFile = 1;
					File backUpFile = new File(backupDir, "backup_" + result.getName());
					while(backUpFile.exists()){
						backUpFile = new File(backupDir, "backup_" + indexResultFile++ + result.getName());
					}
					Files.move(Paths.get(result.getAbsolutePath()), Paths.get(backUpFile.getAbsolutePath()));
				}
				result.createNewFile();

			}
		}
		return result;
	}
}
