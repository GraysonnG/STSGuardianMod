package guardian.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.BronzeScales;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import guardian.GuardianMod;
import guardian.cards.Defend_Guardian;
import guardian.cards.Gem_Red;
import guardian.cards.Strike_Guardian;
import guardian.patches.AbstractCardEnum;
import guardian.patches.GuardianEnum;
import guardian.powers.GuardianModePower;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.characters.CustomCharSelectInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuardianCharacter extends AbstractPlayerWithMinions {
    public static Color cardRenderColor = GuardianMod.mainGuardianColor;
    public float renderscale = 2.5F;

    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    private String atlasURL = "GuardianImages/char/skeleton.atlas";
    private String jsonURL = "GuardianImages/char/skeleton.json";
    private String jsonURLPuddle = "GuardianImages/char/skeletonPuddle.json";

     private String currentJson = jsonURL;

     private boolean inDefensive;



    public static final String[] orbTextures = {"GuardianImages/char/orb/layer1.png", "GuardianImages/char/orb/layer2.png", "GuardianImages/char/orb/layer3.png", "GuardianImages/char/orb/layer4.png", "GuardianImages/char/orb/layer5.png", "GuardianImages/char/orb/layer6.png", "GuardianImages/char/orb/layer1d.png", "GuardianImages/char/orb/layer2d.png", "GuardianImages/char/orb/layer3d.png", "GuardianImages/char/orb/layer4d.png", "GuardianImages/char/orb/layer5d.png"};


    public GuardianCharacter(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "GuardianImages/char/orb/vfx.png", (String) null, (String) null);


        this.initializeClass((String) null, "GuardianImages/char/shoulder2.png", "GuardianImages/char/shoulder.png", "GuardianImages/char/corpse.png", this.getLoadout(), 0.0F, -10F, 310.0F, 260.0F, new EnergyManager(3));
        this.reloadAnimation();


    }

    public CustomCharSelectInfo getInfo() {
        return (CustomCharSelectInfo) getLoadout ();
    }

    //TODO - Victory screens
    /*
    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/greenBg.jpg");

    }


    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel("GuardianImages/scenes/slimebound1.png", "VO_SLIMEBOSS_1A"));
        panels.add(new CutscenePanel("GuardianImages/scenes/slimebound2.png"));
        panels.add(new CutscenePanel("GuardianImages/scenes/slimebound3.png"));
        return panels;
    }
    */




    public void reloadAnimation() {
        this.loadAnimation(atlasURL, this.currentJson, renderscale);
        this.state.setAnimation(0, "idle", true);


    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        this.powers.add(new GuardianModePower(this));
    }

    public void switchToDefensiveMode(){
        if (!inDefensive) {
            CardCrawlGame.sound.play("GUARDIAN_ROLL_UP");
            this.stateData.setMix("idle", "defensive", 0.2F);
            this.state.setTimeScale(.75F);
            this.state.setAnimation(0, "defensive", true);

            inDefensive = true;
        }
    }

    public void switchToOffensiveMode(){
        if (inDefensive) {
            CardCrawlGame.sound.playA("GUARDIAN_ROLL_UP", .25F);
            this.stateData.setMix("defensive", "idle", 0.2F);
            this.state.setTimeScale(.75F);
            this.state.setAnimation(0, "idle", true);

            inDefensive = false;
        }
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList();
        //TODO - Starting deck goes here

        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);

        retVal.add(Gem_Red.ID);
        retVal.add(Gem_Red.ID);

        return retVal;
    }

    public ArrayList<String> getStartingRelics() {

        ArrayList<String> retVal = new ArrayList();
        retVal.add(BronzeScales.ID);
        UnlockTracker.markRelicAsSeen(BronzeScales.ID);
        return retVal;
    }


    public CharSelectInfo getLoadout() {
        return new CustomCharSelectInfo(NAME, DESCRIPTION, 80, 80, 4, 1, 99, 5, this,

                getStartingRelics(), getStartingDeck(), false);

    }

    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.GUARDIAN;
    }

    public Color getCardRenderColor() {

        return cardRenderColor;
    }


    public AbstractCard getStartCardForEvent() {
        //TODO - Strike goes here
        return new Strike_Red();
    }

    public Color getCardTrailColor() {
        return cardRenderColor.cpy();
    }

    public int getAscensionMaxHPLoss() {
        return 10;
    }

    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("MONSTER_GUARDIAN_DESTROY", MathUtils.random(-0.1F, 0.1F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    public String getCustomModeCharacterButtonSoundKey() {
        return "MONSTER_GUARDIAN_DESTROY";
    }

    public String getLocalizedCharacterName() {
        return NAME;
    }

    public AbstractPlayer newInstance() {
        return new GuardianCharacter(NAME, GuardianEnum.GUARDIAN);
    }

    public String getSpireHeartText() {
        return charStrings.TEXT[1] ;
    }

    public Color getSlashAttackColor() {
        return Color.FIREBRICK;
    }

    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AbstractGameAction.AttackEffect.SLASH_HEAVY};
    }

    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[5];
    }

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("Guardian");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];

    }

}


