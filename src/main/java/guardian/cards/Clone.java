package guardian.cards;



import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import guardian.GuardianMod;
import guardian.patches.AbstractCardEnum;
import guardian.powers.ClonePower;

public class Clone extends AbstractGuardianCard {
    public static final String ID = GuardianMod.makeID("Clone");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static String UPGRADED_DESCRIPTION;
    public static final String IMG_PATH = "cards/chargeup.png";

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    //TUNING CONSTANTS

    private static final int COST = 0;
    private static final int COUNT = 1;
    private static final int UPGRADECOUNT = 1;
    private static final int SOCKETS = 1;
    private static final boolean SOCKETSAREAFTER = false;

    //END TUNING CONSTANTS

    public Clone() {
        super(ID, NAME, GuardianMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.GUARDIAN, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = COUNT;

        this.socketCount = SOCKETS;
        this.updateDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p,m);

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ClonePower(p, this.magicNumber), this.magicNumber));

        super.useGems(p,m);
    }

    public AbstractCard makeCopy() {
        return new Clone();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADECOUNT);
            this.rawDescription = UPGRADED_DESCRIPTION;
            this.initializeDescription();
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


