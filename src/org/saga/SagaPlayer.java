package org.saga;

import java.util.*;
import java.io.*;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.saga.professions.*;
import org.saga.utility.*;
import org.saga.exceptions.*;
import org.saga.defaults.*;

public class SagaPlayer {

	// Keys:
	/**
	 * First key groove.
	 */
	private static final String KEY_FIRST_GROOVE= "player_";


	// Balance information keys:
	/**
	 * Last key groove for maximum stamina.
	 */
	private static final String KEY_LAST_GROOVE_MAXIMUM_STAMINA= "maximum_stamina";

	/**
	 * Last key groove for stamina regeneration per second.
	 */
	private static final String KEY_LAST_GROOVE_STAMINA_REGENERATION_PER_SECOND= "stamina_regeneration_per_second";


	// Player information keys:
	/**
	 * Last key groove for stamina value.
	 */
	private static String KEY_LAST_GROOVE_STAMINA= "stamina";


	// Balance information:
	/**
	 * Maximum stamina.
	 */
	private double fMaximumStamina;

	/**
	 * Stamina gain per second.
	 */
	private double fStaminaPerSecond;


	// Player information:
	/**
	 * Stamina.
	 */
	private double fStamina;

	/**
	 * Accessible abilities.
	 */
	private Boolean[] accessibleAbilities= {true, true, true, true}; //TODO Should be loaded from player information


	// Wrapped:
	/**
	 * Plugin instance.
	 */
	private final Saga fPlugin;

	/**
	 * Minecraft player instance.
	 */
	private final String name;


	// Main:
	/**
	 * All professions.
	 */
	private Vector<Profession> fProfessions= new Vector<Profession>();


	// Initialization, saving and loading:
	/**
	 * Initializes by setting all required information.
	 *
	 * @param pPlugin plugin.
	 * @param pPlayer player
	 * @param pBalanceInformation balance information
	 * @param pPlayerInformation player information
	 */
	public SagaPlayer(String name, Properties pBalanceInformation, Properties pPlayerInformation) {

		// Wrap:
		fPlugin= Saga.plugin();
                this.name = name;

		// Initialize all professions:
		//fProfessions.add(new FighterProfession(pBalanceInformation, pPlayerInformation, pPlugin, this));
		//fProfessions.add(new WoodcutterProfession(pBalanceInformation, pPlayerInformation, pPlugin, this));

		// Balance information:
		this.takeBalanceInformation(pBalanceInformation);

		// Player information:
		this.takePlayerInformation(pPlayerInformation);

		// Calculated Values:
		this.refreshCalculatedValues();


	}

        public SagaPlayer(Player player, Properties pBalanceInformation) {

		// Wrap:
		fPlugin = Saga.plugin();
                this.name = player.getName();

                //TODO: Load Default Player Properties here
                Properties pPlayerInformation = new Properties();

		// Initialize all professions:
		//fProfessions.add(new FighterProfession(pBalanceInformation, pPlayerInformation, pPlugin, this));
		//fProfessions.add(new WoodcutterProfession(pBalanceInformation, pPlayerInformation, pPlugin, this));

		// Balance information:
		this.takeBalanceInformation(pBalanceInformation);

		// Player information:
		this.takePlayerInformation(pPlayerInformation);

		// Calculated Values:
		this.refreshCalculatedValues();


	}

        public static SagaPlayer load(String name) throws SagaPlayerNotFoundException {

            // Read player information:
            Properties playerInformation = new Properties();
            Properties balanceInformation = Saga.plugin().getBalanceProperties();

            try {
                playerInformation = WriterReader.readPlayerInformation(name);
            } catch (FileNotFoundException e) {
                Saga.info(name+" player information file not found.");
                throw new SagaPlayerNotFoundException(name);
            } catch (IOException e) {
                Saga.exception(name+" player information load failure",e);
                // TODO Need to disable information saving for this player,
                // because default information will erase all progress
            }

            //Finally just make a new wrap around player
            SagaPlayer player = new SagaPlayer(name,balanceInformation, playerInformation);

            return player;

        }

        public void save() throws IOException {

            WriterReader.writePlayerInformation(name, collectPlayerInformation());

        }

	/**
	 * Takes all balance information and sets fields.
	 * Disables saving and loading if a required value is missing or invalid.
	 *
	 * @param pBalanceInformation balance information
	 */
	private void takeBalanceInformation(Properties pBalanceInformation) {

		// Maximum stamina:
		fMaximumStamina= PlayerDefaults.getDoubleProperty(getKeyBeginning()+KEY_LAST_GROOVE_MAXIMUM_STAMINA, pBalanceInformation, PlayerDefaults.MAXIMUM_STAMINA,fPlugin, getPlayer(), true);
		// Stamina per second:
		fStaminaPerSecond= PlayerDefaults.getDoubleProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA_REGENERATION_PER_SECOND, pBalanceInformation, PlayerDefaults.STAMINA_REGENERATION_PER_SECOND,fPlugin, getPlayer(), true);

	}

	/**
	 * Takes all player information and sets fields.
	 *
	 * @param pPlayerInformation player information
	 */
	private void takePlayerInformation(Properties pPlayerInformation) {


		// Stamina:
		fStamina = PlayerDefaults.getDoubleProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA, pPlayerInformation, PlayerDefaults.STAMINA,fPlugin, getPlayer(), false);


	}

	/**
	 * Refreshes all values that are based on player or balance information.
	 *
	 */
	private void refreshCalculatedValues() {



	}

	/**
	 * Collects all player information for saving.
	 *
	 * @return player information
	 */
	public Properties collectPlayerInformation() {


		Properties playerInformation= new Properties();

		// Collect from here:
		putPlayerInformation(playerInformation);

		// Collect from all professions:
		for (Profession profession : fProfessions) {
			profession.collectPlayerInformation(playerInformation);
		}
		return playerInformation;


	}

	/**
	 * Puts all player information into the given variable.
	 *
	 * @param pPlayerInformation player information
	 */
	private void putPlayerInformation(Properties pPlayerInformation) {


		// Stamina:
		pPlayerInformation.setProperty(getKeyBeginning()+KEY_LAST_GROOVE_STAMINA, fStamina+"");


	}

	// Interaction:
	/**
	 * Returns player.
	 *
	 * @return player
	 */
	public Player getPlayer() {

		return fPlugin.getServer().getPlayer(name);

	}

	// Events:
	/**
	 * Got damaged by living entity event.
	 *
	 * @param pEvent event
	 */
	public void gotDamagedByLivingEntityEvent(EntityDamageByEntityEvent pEvent) {



	}

	/**
	 * Damaged a living entity.
	 *
	 * @param pEvent event
	 */
	public void damagedLivingEntityEvent(EntityDamageByEntityEvent pEvent) {



	}

	/**
	 * Left clicked.
	 *
	 * @param pEvent event
	 */
	public void leftClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Right clicked.
	 *
	 * @param pEvent event
	 */
	public void rightClickInteractEvent(PlayerInteractEvent pEvent) {



	}

	/**
	 * Player placed a block event.
	 *
	 * @param pEvent event
	 */
	public void placedBlockEvent(BlockPlaceEvent pEvent) {



	}

	/**
	 * Player broke a block event.
	 *
	 * @param pEvent event
	 */
	public void brokeBlockEvent(BlockBreakEvent pEvent) {



	}

	/**
	 * Sends a clock tick.
	 *
	 * @param pTick tick number
	 */
	public void clockTickEvent(int pTick) {


	}


	// Keys:
	/**
	 * Returns key beginning.
	 *
	 * @return key beginning
	 */
	private String getKeyBeginning() {

		return KEY_FIRST_GROOVE;

	}

        public void info(String string) {
            this.getPlayer().sendMessage(Constants.infoColor+string);
        }

        public void warning(String string) {
            this.getPlayer().sendMessage(Constants.warningColor+string);
        }

        public void severe(String string) {
            this.getPlayer().sendMessage(Constants.severeColor+string);
        }



}
