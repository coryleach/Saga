package org.saga.professions;

import java.util.Properties;

import org.saga.*;

import org.bukkit.Material;

public class WoodcutterProfession extends Profession {

	public WoodcutterProfession(Properties pBalanceInformation, Properties pPlayerInformation,
			boolean pDefaultBalanceInformation,
			boolean pDefaultPlayerInformation, Saga pPlugin, SagaPlayer pPlayer) {
		
		
		super("woodcutter", pBalanceInformation, pPlayerInformation,
				pDefaultBalanceInformation, pDefaultPlayerInformation, pPlugin, pPlayer);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void takeBalanceInformationSpecific(
			Properties pPlayerInformation, boolean pSetDefaults) {

		// TODO Auto-generated method stub

	}

	@Override
	protected void takePlayerInformationSpecific(Properties pPlayerInformation,
			boolean pSetDefaults) {

		// TODO Auto-generated method stub

	}

	@Override
	protected void putPlayerInformationSpecific(Properties pPlayerInformation) {

		// TODO Auto-generated method stub

	}

	@Override
	protected Material[] getAbilityScrollMaterials() {

		// TODO Auto-generated method stub
		return null;

	}

	@Override
	protected String[] getAbilityNames() {

		// TODO Auto-generated method stub
		return null;

	}

}
