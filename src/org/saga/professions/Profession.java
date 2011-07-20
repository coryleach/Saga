package org.saga.professions;

import java.util.Properties;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.saga.defaults.*;
import org.saga.Saga;
import org.saga.SagaPlayer;


public abstract class Profession {


	// General key grooves:
	/**
	 * First groove for all profession keys.
	 */
	private static final String KEY_FIRST_GROOVE="profession_";

	// Player information key grooves:
	/**
	 * Third groove for level.
	 */
	private static final String KEY_LAST_GROOVE_LEVEL="level";

	/**
	 * Third groove for left experience.
	 */
	private static final String KEY_LAST_GROOVE_LEFT_EXPERIENCE="left_experience";

	/**
	 * Third groove for selected ability.
	 */
	private static final String KEY_LAST_GROOVE_SELECTED_ABILITY="selected_ability";

	// Balance information key grooves:
	/**
	 * Third key groove for stamina function x1.
	 */
	private final String KEY_LAST_GROOVE_STAMINA_FUNCTION_LEVEL_FIRST= "stamina_x1";

	/**
	 * Third key groove for stamina function y1.
	 */
	private final String KEY_LAST_GROOVE_STAMINA_FUNCTION_STAMINA_FIRST= "stamina_y1";

	/**
	 * Third key groove for stamina function x2.
	 */
	private final String KEY_LAST_GROOVE_STAMINA_FUNCTION_LEVEL_SECOND= "stamina_x2";

	/**
	 * Third key groove for stamina function y2.
	 */
	private final String KEY_LAST_GROOVE_STAMINA_FUNCTION_STAMINA_SECOND= "stamina_y2";

	/**
	 * Key for experience intercept.
	 */
	private static String KEY_LAST_GROOVE_EXPERIENCE_INTERCEPT= "experience_intercept";

	/**
	 * Key for experience slope.
	 */
	private static String KEY_LAST_GROOVE_EXPERIENCE_SLOPE= "experience_slope";



	// Wrapped:
	/**
	 * Plugin instance.
	 */
	protected final Saga fPlugin;

	/**
	 * Player information.
	 */
	protected final SagaPlayer fSagaPlayer;


	// Balance information:
	/**
	 * Experience Intercept.
	 */
	private Integer fExperienceIntercept;

	/**
	 * Experience slope.
	 */
	private Integer fExperienceSlope;

	/**
	 * Contains stamina information.
	 * Format: [ability][x1,y1,x2,y2].
	 */
	private Short[][] fStaminaMatrix;


	// Player information:
	/**
	 * Profession level.
	 */
	private Short fLevel;

	/**
	 * Experience left to level up.
	 */
	private Integer fLeftExperience;

	/**
	 * Selected ability.
	 */
	private Short fSelectedAbility;

	/**
	 * Stamina drain for the current level.
	 */
	private Short[] fStaminaDrain;

	/**
	 * Activated abilities.
	 */
	private Boolean[] fActiveAbilities;

	// General:
	/**
	 * Profession name.
	 */
	private final String fProfessionName;


	// Initialization, save and load:
	public Profession(String pProfessionName, Properties pBalanceInformation, Properties pPlayerInformation, Saga pPlugin, SagaPlayer pPlayer) {


		fProfessionName=pProfessionName;
		fPlugin= pPlugin;
		fSagaPlayer= pPlayer;

		// Balance information:
		this.takeBalanceInformation(pBalanceInformation);

		// Player information:
		this.takePlayerInformation(pPlayerInformation);

		// Balance information implementation specific:
		this.takeBalanceInformationSpecific(pBalanceInformation);

		// Player information implementation specific:
		this.takeBalanceInformationSpecific(pPlayerInformation);

		// Calculated Values:
		this.refreshCalculatedValues();


	}

	/**
	 * Takes all balance information and sets fields.
	 * Disables saving and loading if a required value is missing or invalid.
	 *
	 * @param pBalanceInformation balance information
	 */
	private void takeBalanceInformation(Properties pBalanceInformation) {


		//Experience slope:
		fExperienceSlope=PlayerDefaults.getIntegerProperty(getKeyBeginningGeneral()+KEY_LAST_GROOVE_EXPERIENCE_SLOPE, pBalanceInformation, PlayerDefaults.EXPERIENCE_SLOPE, fPlugin, getPlayer(), true);

		//Experience intercept:
		fExperienceIntercept=PlayerDefaults.getIntegerProperty(getKeyBeginningGeneral()+KEY_LAST_GROOVE_EXPERIENCE_INTERCEPT, pBalanceInformation, PlayerDefaults.EXPERIENCE_INTERCEPT, fPlugin, getPlayer(), true);

		// Stamina matrix:
		String[] abilities= getAbilityNames();
		Short[][] matrix= new Short[abilities.length][4];
		for (int i = 0; i < abilities.length; i++) {
			matrix[i][0]= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA_FUNCTION_LEVEL_FIRST+"_"+abilities[i].replaceAll(" ", "_"), pBalanceInformation, PlayerDefaults.STAMINA_FUNCTION_LEVEL_FIRST, fPlugin, getPlayer(), true);
			matrix[i][1]= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA_FUNCTION_STAMINA_FIRST+"_"+abilities[i].replaceAll(" ", "_"), pBalanceInformation, PlayerDefaults.STAMINA_FUNCTION_STAMINA_FIRST, fPlugin, getPlayer(), true);
			matrix[i][0]= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA_FUNCTION_LEVEL_SECOND+"_"+abilities[i].replaceAll(" ", "_"), pBalanceInformation, PlayerDefaults.STAMINA_FUNCTION_LEVEL_SECOND, fPlugin, getPlayer(), true);
			matrix[i][3]= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA_FUNCTION_STAMINA_SECOND+"_"+abilities[i].replaceAll(" ", "_"), pBalanceInformation, PlayerDefaults.STAMINA_FUNCTION_STAMINA_SECOND, fPlugin, getPlayer(), true);
		}
		fStaminaMatrix=matrix;


	}

	/**
	 * Takes all implementation specific balance information and sets fields.
	 * Disables saving and loading if a required value is missing or invalid.
	 *
	 * @param pBalanceInformation balance information
	 */
	protected abstract void takeBalanceInformationSpecific(Properties pPlayerInformation);

	/**
	 * Takes all player information and sets fields.
	 *
	 * @param pPlayerInformation player information
	 */
	private void takePlayerInformation(Properties pPlayerInformation) {

		//Level:
		fLevel= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_LEVEL, pPlayerInformation, PlayerDefaults.LEVEL ,fPlugin, getPlayer(), false);

		//Left experience:
		fLeftExperience= PlayerDefaults.getIntegerProperty(getKeyBeginning()+KEY_LAST_GROOVE_LEFT_EXPERIENCE, pPlayerInformation, calculateExperienceRequirement(fLevel) ,fPlugin, getPlayer(), false);

		//Selected ability:
		fSelectedAbility= PlayerDefaults.getShortProperty(getKeyBeginning()+KEY_LAST_GROOVE_SELECTED_ABILITY, pPlayerInformation, PlayerDefaults.SELECTED_ABILITY ,fPlugin, getPlayer(), false);

	}

	/**
	 * Takes all implementation specific player information and sets fields.
	 *
	 * @param pPlayerInformation player information
	 */
	protected abstract void takePlayerInformationSpecific(Properties pPlayerInformation);

	/**
	 * Refreshes all values that are based on player or balance information.
	 *
	 */
	private void refreshCalculatedValues() {



	}

	/**
	 * Collects all player information into given variable.
	 *
	 * @param pPlayerInformation Player information.
	 */
	public void collectPlayerInformation(Properties pPlayerInformation) {


		// Collect general:
		putPlayerInformation(pPlayerInformation);

		// Collect implementation specific:
		putPlayerInformationSpecific(pPlayerInformation);


	}

	/**
	 * Puts all player information into the given variable.
	 *
	 * @param pPlayerInformation player information
	 */
	private void putPlayerInformation(Properties pPlayerInformation) {

		//Collect general player information:
		//Level:
		pPlayerInformation.setProperty(getKeyBeginning()+KEY_LAST_GROOVE_LEVEL, fLevel+"");

		//Experience:
		pPlayerInformation.setProperty(getKeyBeginning()+KEY_LAST_GROOVE_LEFT_EXPERIENCE, fLeftExperience+"");

		//Selected ability:
		pPlayerInformation.setProperty(getKeyBeginning()+KEY_LAST_GROOVE_SELECTED_ABILITY, fSelectedAbility+"");


	}

	/**
	 * Puts all implementation specific player information into the given variable.
	 *
	 * @param pPlayerInformation Player information
	 */
	protected abstract void putPlayerInformationSpecific(Properties pPlayerInformation);


	// Profession interaction:
	/**
	 * Returns profession name.
	 *
	 * @return profession name
	 */
	public String getProfessionName() {

		return getProfessionName();

	}

	/**
	 * Returns the required experience for level up experience.
	 *
	 * @param pLevel
	 */
	private Integer calculateExperienceRequirement(Short pLevel) {


		return pLevel*fExperienceSlope*pLevel+fExperienceIntercept;


	}

	/**
	 * Returns the array with material names that can be used to scroll trough abilities.
	 *
	 *
	 * @return Materials that can be used to scroll, empty array if none
	 */
	protected abstract Material[] getAbilityScrollMaterials();

	/**
	 * Returns the array with ability names.
	 *
	 *
	 * @return Ability names, empty array if none
	 */
	protected abstract String[] getAbilityNames();

	/**
	 * Returns player.
	 *
	 * @return player
	 */
	public Player getPlayer() {

		return fSagaPlayer.getPlayer();

	}


	// Keys:
	/**
	 * Returns general key beginning.
	 *
	 * @return key beginning
	 */
	private String getKeyBeginningGeneral() {

		return KEY_FIRST_GROOVE;

	}

	/**
	 * Returns key beginning.
	 *
	 * @return key beginning
	 */
	protected String getKeyBeginning() {

		return KEY_FIRST_GROOVE+fProfessionName+"_";

	}


}
