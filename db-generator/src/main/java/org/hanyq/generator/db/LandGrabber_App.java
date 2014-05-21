package org.hanyq.generator.db;

import java.util.ArrayList;
import java.util.List;

import org.hanyq.generator.db.config.Configs;
import org.hanyq.generator.db.meta.DbTable;
import org.hanyq.generator.db.meta.MapperDefinition;
import org.hanyq.generator.db.sourcegenerator.ISourceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.AllTableSourceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.DaoContainerSourceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.DaoSourceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.JavaMapperSouceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.TableSourceGenerator;
import org.hanyq.generator.db.sourcegenerator.impl.XmlMapperSouceGenerator;
import org.hanyq.generator.db.utils.Java2table;

import com.qn.landgrabber.model.army.Army;
import com.qn.landgrabber.model.building.Building;
import com.qn.landgrabber.model.building.folk.Folk;
import com.qn.landgrabber.model.building.market.MarketExchange;
import com.qn.landgrabber.model.farm.Beast;
import com.qn.landgrabber.model.farm.FarmMessage;
import com.qn.landgrabber.model.farm.UserLands;
import com.qn.landgrabber.model.general.General;
import com.qn.landgrabber.model.general.GeneralSoul;
import com.qn.landgrabber.model.general.drill.DrillGround;
import com.qn.landgrabber.model.gobattle.BattleReportData;
import com.qn.landgrabber.model.gobattle.GoBattle;
import com.qn.landgrabber.model.gobattle.GoBattleProgress;
import com.qn.landgrabber.model.gobattle.WarDeclaration;
import com.qn.landgrabber.model.item.Equipment;
import com.qn.landgrabber.model.item.Item;
import com.qn.landgrabber.model.legion.Legion;
import com.qn.landgrabber.model.legion.LegionApply;
import com.qn.landgrabber.model.legion.LegionCreation;
import com.qn.landgrabber.model.legion.LegionInvite;
import com.qn.landgrabber.model.legion.LegionMember;
import com.qn.landgrabber.model.legion.building.LegionBuilding;
import com.qn.landgrabber.model.limit.Limitation;
import com.qn.landgrabber.model.mail.Mail;
import com.qn.landgrabber.model.mail.MailAttachment;
import com.qn.landgrabber.model.pve.PassedCheckpoint;
import com.qn.landgrabber.model.pve.Pve;
import com.qn.landgrabber.model.relation.Relation;
import com.qn.landgrabber.model.talent.Talents;
import com.qn.landgrabber.model.task.TaskCondition;
import com.qn.landgrabber.model.task.TaskRecord;
import com.qn.landgrabber.model.technology.TechnologyLevel;
import com.qn.landgrabber.model.timer.CdTimer;
import com.qn.landgrabber.model.user.User;
import com.qn.landgrabber.model.user.UserExt;
import com.qn.landgrabber.module.rank.impl.UserLevelRank;


public class LandGrabber_App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configs.reload("/configs.xml");
		
		String javaMapperDir = Configs.javaMapperDir;
		String xmlMapperDir = Configs.xmlMapperDir;
		String sqlMapperDir = Configs.sqlMapperDir;
		String daoDir = Configs.daoDir;
		String daoContainerDir = Configs.daoContainerDir;
		
		List<ISourceGenerator<MapperDefinition>> generators = new ArrayList<ISourceGenerator<MapperDefinition>>();
		
		generators.add(new JavaMapperSouceGenerator(javaMapperDir));
		generators.add(new XmlMapperSouceGenerator(xmlMapperDir));
		generators.add(new TableSourceGenerator(sqlMapperDir));
		generators.add(new DaoSourceGenerator(daoDir));
		
		
		
		List<ISourceGenerator<List<MapperDefinition>>> containerGenerators = new ArrayList<ISourceGenerator<List<MapperDefinition>>>();
		containerGenerators.add(new DaoContainerSourceGenerator(daoContainerDir));
		containerGenerators.add(new AllTableSourceGenerator(sqlMapperDir));
		
		List<MapperDefinition> defs = new ArrayList<MapperDefinition>(); 
		defs.add(getUserDef());
		defs.add(getUserExtDef());
		defs.add(getPveDef());
		defs.add(getPassedCheckpointDef());
		defs.add(getTaskRecordDef());
		defs.add(getTaskConditionDef());
		defs.add(getTimerDef());
		defs.add(getBuildingDef());
		defs.add(getLimitDef());
		defs.add(getTechnologyLevelDef());
		defs.add(getTalentDef());
		defs.add(getItemDef());
		defs.add(getEquipmentDef());
		defs.add(getFolkDef());
		defs.add(getRelationDef());
		
		defs.add(getGeneralDef());
		defs.add(getGeneralSoulDef());
		
		defs.add(getArmyDef());
		defs.add(getGoBattleDef());
		defs.add(getGoBattleProgressDef());
		defs.add(getWarDeclarationDef());
		defs.add(getBattleReportDataDef());
		defs.add(getDrillGroundDef());
		defs.add(getMarketExchangeDef());
		
		defs.add(getMailDef());
		defs.add(getMailAttachmentDef());
		
		defs.add(getBeastDef());
		defs.add(getUserLandsDef());
		defs.add(getFarmMessageDef());
		
		defs.add(getLegionCreationDef());
		defs.add(getLegionDef());
		defs.add(getLegionMemberDef());
		defs.add(getLegionApplyDef());
		defs.add(getLegionInviteDef());
		defs.add(getLegionBuilidingDef());
		
		defs.add(getUserLevelRankDef());
		
		
		
		
		for(MapperDefinition def : defs){
			for(ISourceGenerator<MapperDefinition> generator : generators){
				generator.generateSource(def);
			}
		}
		
		for(ISourceGenerator<List<MapperDefinition>> generator : containerGenerators){
			generator.generateSource(defs);
		}
		//containerGenerator.generateSource(defs);
	
		System.err.println("Success!!!");
	}
	
	
	private static MapperDefinition getUserDef(){
		DbTable table = Java2table.java2table(User.class, "userId");
		table.getDbField("signature").setLength(100);
		table.getDbField("infoBrief").setLength(500);
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadUsersByLevel", "level");
		def.addUpdateSql("updateUserLevel", "level", "exp", "battlePower", "infoBrief");
		def.addUpdateSql("updateUserCamp", "camp", "state", "x", "y");
		def.addUpdateSql("updateUserMoney", "coin", "gold", "coupon", "grain", "honor", "energy", "armedForces", "lastEnergyRestoreTime");
		def.addUpdateSql("updateUserAward", "level", "exp", "coin", "gold", "coupon", "grain", "honor", "energy", "armedForces", "battlePower", "infoBrief");
		def.addUpdateSql("updateUserInfoBrief", "battlePower", "infoBrief");
		
		def.addUpdateSql("updateUserSignature", "signature");
		
		def.setLoadMaxSqlName("loadMaxUserId");
		def.setLoadMaxColumn("userId");
		
		return def;
	}
	
	private static MapperDefinition getUserExtDef(){
		DbTable table = Java2table.java2table(UserExt.class, "userId");
		table.getDbField("generalSoulCfgIds").setLength(500);
		MapperDefinition def = new MapperDefinition(table);
		
		def.addUpdateSql("updateTotalTopup", "totalTopup");
		def.addUpdateSql("updateMining", "mineType", "outputInitIron");
		def.addUpdateSql("updateGeneralSoulCfgIds", "generalSoulCfgIds");
		
		def.addUpdateSql("updateLoginCount", "totalLogin", "continuousLogin");
		
		
		return def;
	}
	
	private static MapperDefinition getPveDef(){
		DbTable table = Java2table.java2table(Pve.class, "userId");
		table.getDbField("newPassedCheckpoints").setLength(526);
		MapperDefinition def = new MapperDefinition(table);
		
		def.addUpdateSql("updatePveCollect", "collectTime", "newPassedCheckpoints");
		def.addUpdateSql("updateNewPassedCheckpoints", "newPassedCheckpoints");
		
		
		return def;
	}
	
	private static MapperDefinition getPassedCheckpointDef(){
		DbTable table = Java2table.java2table(PassedCheckpoint.class, "userId", "checkpointId");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadPassedCheckpointsByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getTaskRecordDef(){
		DbTable table = Java2table.java2table(TaskRecord.class, "userId");
		table.getDbField("onGoingMainLines").setLength(500);
		table.getDbField("waitingLevelUpTasks").setLength(500);
		MapperDefinition def = new MapperDefinition(table);
		
		return def;
	}
	
	private static MapperDefinition getTaskConditionDef(){
		DbTable table = Java2table.java2table(TaskCondition.class, "userId", "taskId");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadTaskConditionsByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getTimerDef(){
		DbTable table = Java2table.java2table(CdTimer.class, "userId", "timerId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadTimersByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getBuildingDef(){
		DbTable table = Java2table.java2table(Building.class, "userId", "buildingId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadBuildingsByUser", "userId");
		def.addUpdateSql("updateBuildingLevel", "buildingLevel");
		def.addUpdateSql("updateBuildingAccumulation", "accumulation", "updateTime");
		
		return def;
	}
	
	private static MapperDefinition getLimitDef(){
		DbTable table = Java2table.java2table(Limitation.class, "userId", "limitId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadLimitationsByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getTechnologyLevelDef(){
		DbTable table = Java2table.java2table(TechnologyLevel.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		
		return def;
	}
	
	private static MapperDefinition getTalentDef(){
		DbTable table = Java2table.java2table(Talents.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		
		return def;
	}
	
	private static MapperDefinition getItemDef(){
		DbTable table = Java2table.java2table(Item.class, "itemId");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadItemsByUser", "userId");
		
		def.setLoadMaxSqlName("loadMaxItemId");
		def.setLoadMaxColumn("itemId");
		
		return def;
	}
	
	private static MapperDefinition getEquipmentDef(){
		DbTable table = Java2table.java2table(Equipment.class, "itemId");
		MapperDefinition def = new MapperDefinition(table);
		def.setLoadByIdsSqlName("loadEquipmentByItemIds");
		def.setLoadByCollectionColumn("itemId");
		
		/*def.addLoadSql("loadItemsByItemIds", "userId");
		
		def.setLoadMaxSqlName("loadMaxItemId");
		def.setLoadMaxColumn("itemId");*/
		
		def.addUpdateSql("updateEquipmentLevel", "level");
		
		
		return def;
	}
	
	private static MapperDefinition getFolkDef(){
		DbTable table = Java2table.java2table(Folk.class, "userId", "folkId");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadFolksByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getRelationDef(){
		DbTable table = Java2table.java2table(Relation.class, "userId");
		table.getDbField("friendIds").setLength(1024);
		table.getDbField("recommendFriendIds").setLength(1024);
		table.getDbField("enemyIds").setLength(1024);
		
		MapperDefinition def = new MapperDefinition(table);
		
		def.addUpdateSql("updateFriends", "friendIds");
		def.addUpdateSql("updateRecommendFriends", "recommendFriendIds");
		def.addUpdateSql("updateEnemys", "enemyIds");
		
		def.addUpdateSql("updateFriendsAndRecommendFriends", "friendIds", "recommendFriendIds");
		
		return def;
	}
	
	private static MapperDefinition getGeneralDef(){
		DbTable table = Java2table.java2table(General.class, "userId", "generalId");
		
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadGeneralsByUser", "userId");
		
		def.addUpdateSql("updateGeneralExp", "level", "exp");
		
		def.addUpdateSql("updateGeneralTrainedRate", "hpTrainedRate", "attackTrainedRate", "speedTrainedRate", "armyForcesTrainedRate");
		
		def.addUpdateSql("updateGeneralElevate", "generalCfgId", "openAttrCount");
		
		return def;
	}
	
	private static MapperDefinition getGeneralSoulDef(){
		DbTable table = Java2table.java2table(GeneralSoul.class, "userId", "soulId");
		
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadGeneralSoulsByUser", "userId");
		
		def.addUpdateSql("updateGeneralSoulExp", "level", "exp");
		
		return def;
	}
	
	private static MapperDefinition getArmyDef(){
		DbTable table = Java2table.java2table(Army.class, "userId", "armyType");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadArmysByUser", "userId");
		
		def.addUpdateSql("updateArmyInfo", "generalId", "position", "leadTroops");
		
		def.addUpdateSql("updateArmyEquipments", "weaponItemId", "helmetItemId", "breastplateItemId", "shoesItemId", "necklaceItemId");
		
		def.addUpdateSql("updateSoulFormation", "soulFormationP0", "soulFormationP1", "soulFormationP2", "soulFormationP3", "soulFormationP4");
	    
		return def;
	}
	
	private static MapperDefinition getGoBattleDef(){
		DbTable table = Java2table.java2table(GoBattle.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadGoBattlesOfOnGoing", "onGoing");
		
		return def;
	}
	
	private static MapperDefinition getGoBattleProgressDef(){
		DbTable table = Java2table.java2table(GoBattleProgress.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadGoBattleProcessesOfOnGoing", "onGoing");
		
		return def;
	}
	
	private static MapperDefinition getWarDeclarationDef(){
		DbTable table = Java2table.java2table(WarDeclaration.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadWarDeclarationsByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getBattleReportDataDef(){
		DbTable table = Java2table.java2table(BattleReportData.class, "battleReportId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.setLoadMaxSqlName("loadMaxBattleReportId");
		def.setLoadMaxColumn("battleReportId");
		
		return def;
	}
	
	private static MapperDefinition getDrillGroundDef(){
		DbTable table = Java2table.java2table(DrillGround.class, "userId", "drillGroundId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadDrillGroundsByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getMarketExchangeDef(){
		DbTable table = Java2table.java2table(MarketExchange.class, "userId", "armyType");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadMarketExchangesByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getMailDef(){
		DbTable table = Java2table.java2table(Mail.class, "mailId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.setLoadMaxSqlName("loadMaxMailId");
		def.setLoadMaxColumn("mailId");
		
		def.addLoadSql("loadMailsByUser", "receiveUserId");
		
		def.addUpdateSql("updateMailMarkRead", "hasRead");
		
		return def;
	}
	
	private static MapperDefinition getMailAttachmentDef(){
		DbTable table = Java2table.java2table(MailAttachment.class, "mailId", "attachmentId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.setLoadByIdsSqlName("loadMailAttachmentsByMailIds");
		def.setLoadByCollectionColumn("mailId");
		
		def.addDeleteSql("deleteMailAttachmentsByMailId", "mailId");
		
		return def;
	}
	
	private static MapperDefinition getUserLandsDef(){
		DbTable table = Java2table.java2table(UserLands.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		return def;
	}
	
	private static MapperDefinition getFarmMessageDef(){
		DbTable table = Java2table.java2table(FarmMessage.class, "userId", "messageId");
		table.getDbField("message").setLength(500);
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadFarmMessagesByUser", "userId");
		
		return def;
	}
	
	private static MapperDefinition getBeastDef(){
		DbTable table = Java2table.java2table(Beast.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		return def;
	}
	
	private static MapperDefinition getLegionCreationDef(){
		DbTable table = Java2table.java2table(LegionCreation.class, "creatorId");
		table.getDbField("inviteStates").setLength(500);
		MapperDefinition def = new MapperDefinition(table);

		
		return def;
	}
	
	
	private static MapperDefinition getLegionDef(){
		DbTable table = Java2table.java2table(Legion.class, "legionId");
		table.getDbField("comment").setLength(500);
		MapperDefinition def = new MapperDefinition(table);
		
		def.setLoadMaxSqlName("loadMaxLegionId");
		def.setLoadMaxColumn("legionId");
		
		return def;
	}
	
	private static MapperDefinition getLegionMemberDef(){
		DbTable table = Java2table.java2table(LegionMember.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		return def;
	}
	
	private static MapperDefinition getLegionApplyDef(){
		DbTable table = Java2table.java2table(LegionApply.class, "userId", "legionId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addDeleteSql("deleteLegionApplysByUserId", "userId");
		def.addDeleteSql("deleteLeginApplysByLegionId", "legionId");
		
		return def;
	}
	
	private static MapperDefinition getLegionInviteDef(){
		DbTable table = Java2table.java2table(LegionInvite.class, "receiveUserId", "legionId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addDeleteSql("deleteLegionInvitesByUserId", "receiveUserId");
		def.addDeleteSql("deleteLeginInvitesByLegionId", "legionId");
		
		return def;
	}
	
	private static MapperDefinition getLegionBuilidingDef(){
		DbTable table = Java2table.java2table(LegionBuilding.class, "legionId", "legionBuildingId");
		MapperDefinition def = new MapperDefinition(table);
		
		return def;
	}
	
	
	private static MapperDefinition getUserLevelRankDef(){
		DbTable table = Java2table.java2table(UserLevelRank.class, "ranking");
		MapperDefinition def = new MapperDefinition(table);
		
		def.setDeleteAll(true);
		
		return def;
	}
}
