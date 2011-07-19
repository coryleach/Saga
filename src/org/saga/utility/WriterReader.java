package me.andf54.factio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriterReader {

	/**
	 * Main directory for plugin files.
	 */
	private static String MAIN_DIRECTORY="plugins"+File.separator+"Factio";
	
	/**
	 * Player information directory.
	 */
	private static String PLAYER_INFORMATION_DIRECTORY=MAIN_DIRECTORY+File.separator+"players";
	
	/**
	 * Balance information location.
	 */
	private static String BALANCE_INFORMATION_DIRECTORY=MAIN_DIRECTORY+"/balanceinformation.dat";
	
	
	/**
	 * Reads the users player information.
	 * 
	 * @param pPlayerName Player name
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while reading
	 */
	public static Properties readPlayerInformation(String pPlayerName) throws IOException {

		
		Properties playerInfo= new Properties();
		FileInputStream in = new FileInputStream(new File(PLAYER_INFORMATION_DIRECTORY+File.separator+pPlayerName+".dat"));
		playerInfo.load(in);
		in.close();
		return playerInfo;
		
		
	}
	
	/**
	 * Writes the users player information.
	 * 
	 * @param pPlayerName Player name
	 * @param playerInfo Player information
	 * 
	 * @return Player information
	 * 
	 * @throws IOException If an error occurred while writing.
	 */
	public static void writePlayerInformation(String pPlayerName, Properties playerInfo) throws IOException {

		
		File directory= new File(PLAYER_INFORMATION_DIRECTORY+File.separator+pPlayerName+".dat");
		if(!(new File(PLAYER_INFORMATION_DIRECTORY+File.separator).exists())){
			(new File(PLAYER_INFORMATION_DIRECTORY+File.separator)).mkdirs();
			System.out.println("Creating "+PLAYER_INFORMATION_DIRECTORY+File.separator+" directory.");
		}
		if(!directory.exists()){
			directory.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(directory);
		playerInfo.store(out, pPlayerName+" player information");
		out.flush();
		out.close();
		
		
	}
	
	
	/**
	 * Reads balance information
	 * 
	 * @return Balance information
	 * @throws IOException If an error occurred while reading
	 */
	public static Properties readBalanceInformation() throws IOException {

		Properties balanceInfo= new Properties();
		FileInputStream in = new FileInputStream(new File(BALANCE_INFORMATION_DIRECTORY));
		balanceInfo.load(in);
		in.close();
		return balanceInfo;
		
		
	}
	
	
}
