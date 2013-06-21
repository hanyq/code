package com.kingdowin.config.db;

import java.util.ArrayList;
import java.util.List;

import com.kingdowin.config.db.meta.DbTable;
import com.kingdowin.config.db.meta.MapperDefinition;
import com.kingdowin.config.db.sourcegenerator.ISourceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.AllTableSourceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.DaoContainerSourceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.DaoSourceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.JavaMapperSouceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.TableSourceGenerator;
import com.kingdowin.config.db.sourcegenerator.impl.XmlMapperSouceGenerator;
import com.kingdowin.config.db.utils.Java2table;
import com.kingdowin.newlol.domain.hero.Card;
import com.kingdowin.newlol.domain.item.Item;
import com.kingdowin.newlol.domain.user.Account;
import com.kingdowin.newlol.domain.user.User;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String javaMapperDir = "E:\\Workspace\\newlol-backend\\src\\main\\java\\com\\kingdowin\\newlol\\db\\mapper\\";
		String xmlMapperDir = "E:\\Workspace\\newlol-backend\\src\\main\\resources\\config\\db\\mapper\\";
		String sqlMapperDir = "E:\\Workspace\\newlol-backend\\src\\main\\resources\\config\\db\\sql\\";
		String daoDir = "E:\\Workspace\\newlol-backend\\src\\main\\java\\com\\kingdowin\\newlol\\db\\dao\\";
		String daoContainerDir = "E:\\Workspace\\newlol-backend\\src\\main\\java\\com\\kingdowin\\newlol\\db\\";
		
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
		defs.add(getAccountDef());
		defs.add(getCardDef());
		defs.add(getItemDef());
		
		
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
		DbTable table = Java2table.java2table(Account.class, "account");
		MapperDefinition def = new MapperDefinition(table);
		
		def.addUpdateSql("updateAccountTime", "createTime", "loginTime", "lastActiveTime");
		
		
		return def;
	}
	
	private static MapperDefinition getCardDef(){
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
	}
}
