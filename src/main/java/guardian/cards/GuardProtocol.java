package guardian.cards;



import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import guardian.GuardianMod;
import guardian.actions.SpawnBronzeOrbAction;
import guardian.patches.AbstractCardEnum;
import guardian.powers.BronzeOrbBlockPower;
import guardian.powers.BronzeOrbDamagePower;
import guardian.powers.BronzeOrbWeakenPower;

public class GuardProtocol extends AbstractGuardianCard {
    public static final String ID = GuardianMod.makeID("GuardProtocol");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static String UPGRADED_DESCRIPTION;
    public static final String IMG_PATH = "cards/defendSlime.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    //TUNING CONSTANTS

    private static final int COST = 1;
    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 2;
    private static final int SOCKETS = 0;
    private static final boolean SOCKETSAREAFTER = true;

    //END TUNING CONSTANTS

    public GuardProtocol() {
        super(ID, NAME, GuardianMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.GUARDIAN, RARITY, TARGET);


        this.baseMagicNumber = this.magicNumber = BLOCK;

        this.socketCount = SOCKETS;
        this.updateDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p,m);
        if (GuardianMod.bronzeOrbInPlay == null){
            AbstractDungeon.actionManager.addToBottom(new SpawnBronzeOrbAction(AbstractDungeon.player, SpawnBronzeOrbAction.powerTypes.DEFEND, this.magicNumber, 0));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(GuardianMod.bronzeOrbInPlay, p, new BronzeOrbBlockPower(GuardianMod.bronzeOrbInPlay, this.magicNumber), this.magicNumber));
        }
    }

    public AbstractCard makeCopy() {
        return new GuardProtocol();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BLOCK);
        }
    }

    public void updateDescription() {
        if (SOCKETS > 0) this.rawDescription = this.updateGemDescription(cardStrings.DESCRIPTION, SOCKETSAREAFTER);
        //GuardianMod.logger.info(DESCRIPTION);
        this.initializeDescription();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}


