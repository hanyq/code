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

import com.qn.landgrabber.model.user.User;
import com.qn.loginserver.model.account.Account;
import com.qn.loginserver.model.server.GameServer;


public class Loginserver_App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configs.reload("/loginserver_configs.xml");
		
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
		//containerGenerators.add(new DaoContainerSourceGenerator(daoContainerDir));
		containerGenerators.add(new AllTableSourceGenerator(sqlMapperDir));
		
		List<MapperDefinition> defs = new ArrayList<MapperDefinition>(); 
		//defs.add(getUserDef());
		defs.add(getAccountDef());
		//defs.add(getCardDef());
		defs.add(getGameServerDef());
		
		
		for(MapperDefinition def : defs){
			for(ISourceGenerator<MapperDefinition> generator : generators){
				generator.generateSource(def);
			}
		}
		
		for(ISourceGenerator<List<MapperDefinition>> generator : containerGenerators){
			generator.generateSource(defs);
		}
		//containerGenerator.generateSource(defs);
		
	}
	
	
	private static MapperDefinition getUserDef(){
		DbTable table = Java2table.java2table(User.class, "userId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadUsersByLevel", "level");
		def.addUpdateSql("updateUserLevel", "level", "exp");
		
		def.setLoadMaxSqlName("loadMaxUserId");
		def.setLoadMaxColumn("userId");
		
		return def;
	}
	
	private static MapperDefinition getAccountDef(){
		DbTable table = Java2table.java2table(Account.class, "accountId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadAccountByKey", "loginType", "account");
		def.addUpdateSql("updateAccountLogin", "token", "loginTime", "password");
		def.addUpdateSql("updateAccountLoginServer", "lastLoginServer");
		
		def.setLoadMaxSqlName("loadMaxAccountId");
		def.setLoadMaxColumn("accountId");
		
		return def;
	}
	
	private static MapperDefinition getGameServerDef(){
		DbTable table = Java2table.java2table(GameServer.class, "serverId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addUpdateSql("updateServerState", "state");
		
		
		return def;
	}
	
/*	private static MapperDefinition getCardDef(){
		DbTable table = Java2table.java2table(Card.class, "cardId");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addLoadSql("loadCardsByUser", "userId");
		
		//def.addUpdateSql("updateAccountTime", "createTime", "loginTime", "lastActiveTime");
		def.setLoadMaxSqlName("loadMaxCardId");
		def.setLoadMaxColumn("cardId");
		
		return def;
	}
	
	private static MapperDefinition getItemDef(){
		DbTable table = Java2table.java2table(Item.class, "itemId");
		MapperDefinition def = new MapperDefinition(table);
		def.addLoadSql("loadItemsByUser", "userId");
		
		//def.addUpdateSql("updateAccountTime", "createTime", "loginTime", "lastActiveTime");
		def.setLoadMaxSqlName("loadMaxItemId");
		def.setLoadMaxColumn("itemId");
		
		return def;
	}*/
}
