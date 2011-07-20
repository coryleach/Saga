package org.saga.defaults;

import java.util.Properties;
import org.bukkit.entity.Player;
import org.saga.*;

public class PlayerDefaults {


	// Balance:
	public static Double MAXIMUM_STAMINA= 100.0;

	public static Double STAMINA_REGENERATION_PER_SECOND= 0.1;

	public static Short STAMINA_FUNCTION_LEVEL_FIRST= 100;

	public static Short STAMINA_FUNCTION_STAMINA_FIRST= 10000;

	public static Short STAMINA_FUNCTION_LEVEL_SECOND= 101;

	public static Short STAMINA_FUNCTION_STAMINA_SECOND= 10000;

	public static Integer EXPERIENCE_SLOPE= 1000000;

	public static Integer EXPERIENCE_INTERCEPT= 1000000;


	// Player:
	public static Double STAMINA= 50.0;

	public static Short LEVEL= 0;

	public static Short SELECTED_ABILITY= 0;


	public static Double getDoubleProperty(String pKey, Properties pProperties, Double pDefaultValue, Saga pSaga, Player pPlayer, boolean pIsBalanceInformation) {


		String stringValue= pProperties.getProperty(pKey);
		if(stringValue==null){
			if(pIsBalanceInformation){
				Saga.severe("Balance information not found for "+pKey+" key. Setting default.", pPlayer);
			}
			return pDefaultValue;
		}
		try {
			return Double.parseDouble(stringValue);
		} catch (NumberFormatException e) {
			if(pIsBalanceInformation){
				Saga.severe("Balance information parse failure for "+pKey+" key. Setting default.", pPlayer);
			}else{
				Saga.severe("Player information parse failure for "+pKey+" key. Setting default.", pPlayer);
			}
			return pDefaultValue;
		}


	}

	public static Integer getIntegerProperty(String pKey, Properties pProperties, Integer pDefaultValue, Saga pSaga, Player pPlayer, boolean pIsBalanceInformation) {


			String stringValue= pProperties.getProperty(pKey);
			if(stringValue==null){
				if(pIsBalanceInformation){
					Saga.severe("Balance information not found for "+pKey+" key. Setting default.", pPlayer);
				}
				return pDefaultValue;
			}
			try {
				return Integer.parseInt(stringValue);
			} catch (NumberFormatException e) {
				if(pIsBalanceInformation){
					Saga.severe("Balance information parse failure for "+pKey+" key. Setting default.", pPlayer);
				}else{
					Saga.severe("Player information parse failure for "+pKey+" key. Setting default.", pPlayer);
				}
				return pDefaultValue;
			}


	}

	public static Short getShortProperty(String pKey, Properties pProperties, Short pDefaultValue, Saga pSaga, Player pPlayer, boolean pIsBalanceInformation) {


		String stringValue= pProperties.getProperty(pKey);
		if(stringValue==null){
			if(pIsBalanceInformation){
				Saga.severe("Balance information not found for "+pKey+" key. Setting default.", pPlayer);
			}
			return pDefaultValue;
		}
		try {
			return Short.parseShort(stringValue);
		} catch (NumberFormatException e) {
			if(pIsBalanceInformation){
				Saga.severe("Balance information parse failure for "+pKey+" key. Setting default.", pPlayer);
			}else{
				Saga.severe("Player information parse failure for "+pKey+" key. Setting default.", pPlayer);
			}
			return pDefaultValue;
		}


	}





}






