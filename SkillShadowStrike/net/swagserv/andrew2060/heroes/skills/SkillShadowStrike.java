package net.swagserv.andrew2060.heroes.skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.api.events.SkillUseEvent;
import com.herocraftonline.heroes.characters.CharacterTemplate;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.ExpirableEffect;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.TargettedSkill;

public class SkillShadowStrike extends TargettedSkill {

	public class SilenceListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onSkillUse(SkillUseEvent event) {
			Hero h = event.getHero();
			if(h.hasEffect("ShadowStrikeSilence")) {
				h.getPlayer().sendMessage(ChatColor.GRAY + "You are silenced and cannot use skills");
				event.setCancelled(true);
			}
		}
	}

	public SkillShadowStrike(Heroes plugin) {
		super(plugin, "ShadowStrike");
		setDescription("Blinks to a target, silencing them, dealing 5% max health damage, and applying a 3 second 30% slow.");
		setUsage("/skill shadowstrike");
		setIdentifiers("skill shadowstrike");
		setArgumentRange(0,1);
		Bukkit.getPluginManager().registerEvents(new SilenceListener(), this.plugin);
	}

	@Override
	public String getDescription(Hero arg0) {
		return getDescription();
	}

	@Override
	public SkillResult use(Hero h, LivingEntity lE, String[] args) {
		CharacterTemplate cT = this.plugin.getCharacterManager().getCharacter(lE);
		h.getPlayer().teleport(lE.getLocation(), TeleportCause.PLUGIN);
		cT.addEffect(new ExpirableEffect(this, plugin, "ShadowStrikeSilence", 4000));
		lE.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 2));
		Skill.damageEntity(lE, h.getEntity(), (int) (cT.getMaxHealth()*5*0.01), DamageCause.ENTITY_ATTACK);
		return SkillResult.NORMAL;
	}

}
