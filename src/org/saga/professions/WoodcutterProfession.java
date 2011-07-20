package org.saga.professions;

import java.util.Properties;


import org.bukkit.Material;
import org.saga.Saga;
import org.saga.SagaPlayer;

public class WoodcutterProfession extends Profession {


	/**
	 * Profession name.
	 */
	private static final String PROFESSION_NAME="woodcutter";

	/**
	 * Ability scroll materials.
	 */
	private static final Material[] SCROLL_MATERIALS={Material.DIAMOND_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD, Material.WOOD_SWORD};

	/**
	 * Ability names.
	 */
	private static final String[] ABILITY_NAMES={"none", "chop down","supply chest","self defence"};


	// Initialization, loading and saving:
	public WoodcutterProfession(Properties pBalanceInformation, Properties pPlayerInformation, Saga pPlugin, SagaPlayer pPlayer) {

		super(PROFESSION_NAME, pBalanceInformation, pPlayerInformation, pPlugin, pPlayer);

		// Balance information:
		takeBalanceInformationSpecific(pBalanceInformation);

		// Player information:
		takePlayerInformationSpecific(pPlayerInformation);


	}

	/*
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#takeBalanceInformationSpecific(java.util.Properties)
	 */
	@Override
	protected void takeBalanceInformationSpecific(Properties pPlayerInformation) {

		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#takePlayerInformationSpecific(java.util.Properties)
	 */
	@Override
	protected void takePlayerInformationSpecific(Properties pPlayerInformation) {

		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#putPlayerInformationSpecific(java.util.Properties)
	 */
	@Override
	protected void putPlayerInformationSpecific(Properties pPlayerInformation) {

		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#getAbilityScrollMaterials()
	 */
	@Override
	protected Material[] getAbilityScrollMaterials() {

		return SCROLL_MATERIALS;

	}

	/*
	 * (non-Javadoc)
	 * @see org.saga.professions.Profession#getAbilityNames()
	 */
	@Override
	protected String[] getAbilityNames() {

		return ABILITY_NAMES;

	}

}
