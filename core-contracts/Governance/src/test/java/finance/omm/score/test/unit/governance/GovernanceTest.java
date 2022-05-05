package finance.omm.score.test.unit.governance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.iconloop.score.test.Account;
import finance.omm.libs.structs.AssetConfig;
import finance.omm.libs.structs.governance.ReserveAttributes;
import finance.omm.libs.structs.governance.ReserveConstant;
import finance.omm.utils.constants.TimeConstants;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import score.Address;

public class GovernanceTest extends AbstractGovernanceTest {
    @Test
    public void name() {
        String actual = (String) score.call("name");
        String expected = "Omm Governance Manager";
        assertEquals(expected, actual);
    }

    @Test
    public void setReserveActiveStatus() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        boolean status = false;
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateIsActive(reserve, status);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "setReserveActiveStatus", reserve, status);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "setReserveActiveStatus", reserve, status);
        verify(scoreSpy, times(1));
    }

    @Test
    public void setReserveFreezeStatus() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        boolean status = false;
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateIsFreezed(reserve, status);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "setReserveFreezeStatus", reserve, status);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "setReserveFreezeStatus", reserve, status);
        verify(scoreSpy, times(1));
    }

    @Test
    public void setReserveConstants() {
        ReserveConstant constant = new ReserveConstant();
        ReserveConstant[] constants = new ReserveConstant[1];

        constant.reserve = Account.newScoreAccount(2).getAddress();
        constant.baseBorrowRate = TWO.multiply(PERCENT);
        constant.optimalUtilizationRate = EIGHTY.multiply(PERCENT);
        constant.slopeRate1 = TWO.multiply(ICX);
        constant.slopeRate2 = BigInteger.valueOf(100L).multiply(ICX);

        constants[0] = constant;

        Account notOwner = sm.createAccount();
        Object[] params = new Object[]{constants};

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).setReserveConstants(constants);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "setReserveConstants", params);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "setReserveConstants", params);
        verify(scoreSpy, times(1));
    }

    private ReserveAttributes createReserveAttributes(int index) {
        ReserveAttributes reserveData = new ReserveAttributes();
        reserveData.reserveAddress = Account.newScoreAccount(index).getAddress();
        reserveData.oTokenAddress = Account.newScoreAccount(index+1).getAddress();
        reserveData.dTokenAddress = Account.newScoreAccount(index+2).getAddress();
        reserveData.lastUpdateTimestamp = TimeConstants.getBlockTimestamp();
        reserveData.liquidityRate = BigInteger.ZERO;
        reserveData.borrowRate = BigInteger.ZERO;
        reserveData.liquidityCumulativeIndex = ICX;
        reserveData.borrowCumulativeIndex = ICX;
        reserveData.baseLTVasCollateral = EIGHTY.multiply(PERCENT);
        reserveData.liquidationThreshold = TEN.multiply(PERCENT);
        reserveData.liquidationBonus = TEN.multiply(PERCENT);
        reserveData.decimals = 18;
        reserveData.borrowingEnabled = true;
        reserveData.usageAsCollateralEnabled = true;
        reserveData.isFreezed = true;
        reserveData.isActive = true;
        return  reserveData;
    }

    @Test
    public void initializeReserve() {

        ReserveAttributes reserveData = createReserveAttributes(1);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).addReserveData(reserveData);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "initializeReserve", reserveData);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "initializeReserve", reserveData);
    }

    @Test
    public void updateBaseLTVasCollateral() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        BigInteger baseLtv = HUNDRED.multiply(PERCENT);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateBaseLTVasCollateral(reserve, baseLtv);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateBaseLTVasCollateral", reserve, baseLtv);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateBaseLTVasCollateral", reserve, baseLtv);
        verify(scoreSpy, times(1));
    }

    @Test
    public void updateLiquidationThreshold() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        BigInteger liquidationThreshold = EIGHTY.multiply(PERCENT);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateLiquidationThreshold(reserve, liquidationThreshold);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateLiquidationThreshold", reserve, liquidationThreshold);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateLiquidationThreshold", reserve, liquidationThreshold);
        verify(scoreSpy, times(1));
    }

    @Test
    public void updateBorrowThreshold() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        BigInteger borrowThreshold = EIGHTY.multiply(PERCENT);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateBorrowThreshold(reserve, borrowThreshold);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateBorrowThreshold", reserve, borrowThreshold);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateBorrowThreshold", reserve, borrowThreshold);
        verify(scoreSpy, times(1));
    }

    @Test
    public void updateLiquidationBonus() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        BigInteger liquidationBonus = TEN.multiply(PERCENT);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateLiquidationBonus(reserve, liquidationBonus);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateLiquidationBonus", reserve, liquidationBonus);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateLiquidationBonus", reserve, liquidationBonus);
        verify(scoreSpy, times(1));
    }

    @Test
    public void updateBorrowingEnabled() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        boolean status = false;
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateBorrowingEnabled(reserve, status);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateBorrowingEnabled", reserve, status);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateBorrowingEnabled", reserve, status);
        verify(scoreSpy, times(1));
    }

    @Test
    public void updateUsageAsCollateralEnabled() {
        Address reserve = Account.newScoreAccount(1).getAddress();
        boolean status = false;
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.lendingPoolCore).updateUsageAsCollateralEnabled(reserve, status);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "updateUsageAsCollateralEnabled", reserve, status);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "updateUsageAsCollateralEnabled", reserve, status);
        verify(scoreSpy, times(1));
    }

    @Test
    public void enableRewardClaim() {
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.rewardDistribution).enableRewardClaim();

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "enableRewardClaim");
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "enableRewardClaim");
        verify(scoreSpy, times(1));
    }

    @Test
    public void disableRewardClaim() {
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.rewardDistribution).disableRewardClaim();

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "disableRewardClaim");
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "disableRewardClaim");
        verify(scoreSpy, times(1));
    }

    private AssetConfig createAssetConfig(int i) {
        AssetConfig config = new AssetConfig();
        config.poolID = i;
        config.asset = Account.newScoreAccount(i).getAddress();
        config.distPercentage = TEN.multiply(PERCENT);
        config.assetName = "Asset "+ i;
        config.rewardEntity = "lendingBorrow";
        return config;
    }

    @DisplayName("Add pool method for LP tokens")
    @Test
    public void addPoolLP() {
        int poolId = 1;
        AssetConfig config = createAssetConfig(poolId);
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.stakedLP).addPool(poolId, config.asset);
        doNothing().when(scoreSpy.rewardDistribution).configureAssetConfig(config);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "addPool", config);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "addPool", config);
        verify(scoreSpy, times(1));
    }

    @DisplayName("Add pool method for non LP tokens")
    @Test
    public void addPool() {
        int poolId = 0;
        AssetConfig config = createAssetConfig(poolId);

        // mock class client and call the method
        doNothing().when(scoreSpy.rewardDistribution).configureAssetConfig(config);

        // owner
        score.invoke(owner, "addPool", config);
        verify(scoreSpy, times(1));
    }

    @Test
    public void addPools() {
        AssetConfig[] configs = new AssetConfig[3];

        AssetConfig config1 = createAssetConfig(0);
        AssetConfig config2 = createAssetConfig(0);
        AssetConfig config3 = createAssetConfig(2);
        configs[0] = config1;
        configs[1] = config2;
        configs[2] = config3;
        Object[] params = new Object[]{configs};

        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doNothing().when(scoreSpy.stakedLP).addPool(2, config3.asset);
        doNothing().when(scoreSpy.rewardDistribution).configureAssetConfig(any());

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "addPools", params);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "addPools", params);
    }

    @Test
    public void removePool() {

        /*
         * for LP Tokens
         */
        BigInteger poolId = ONE;
        Address asset = Account.newScoreAccount(1).getAddress();
        Account notOwner = sm.createAccount();

        // mock class client and call the method
        doReturn(poolId).when(scoreSpy.rewardDistribution).getPoolIDByAsset(asset);
        doNothing().when(scoreSpy.stakedLP).removePool(poolId.intValue());
        doNothing().when(scoreSpy.rewardDistribution).removeAssetConfig(asset);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "removePool", asset);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "removePool", asset);

        /*
         * For non-lp tokens
         */
        poolId = BigInteger.ZERO;

        doReturn(poolId).when(scoreSpy.rewardDistribution).getPoolIDByAsset(asset);
        doNothing().when(scoreSpy.rewardDistribution).removeAssetConfig(asset);

        // not owner
        notOwnerCall = () -> score.invoke(notOwner, "removePool", asset);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "removePool", asset);
    }


    @Test
    public void transferOmmToDaoFund() {
        Account notOwner = sm.createAccount();
        BigInteger value = ICX;

        doNothing().when(scoreSpy.rewardDistribution).transferOmmToDaoFund(value);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "transferOmmToDaoFund", value);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "transferOmmToDaoFund", value);
    }

    @Test
    public void transferOmmFromDaoFund() {
        Account notOwner = sm.createAccount();
        Address randomAddr = sm.createAccount().getAddress();
        BigInteger value = ICX;

        doNothing().when(scoreSpy.daoFund).transferOmm(value, randomAddr);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "transferOmmFromDaoFund", value, randomAddr);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "transferOmmFromDaoFund", value, randomAddr);
    }

    @Test
    public void transferFundFromFeeProvider() {
        Account notOwner = sm.createAccount();
        Address token = Account.newScoreAccount(1).getAddress();
        Address randomAddr = sm.createAccount().getAddress();
        BigInteger value = ICX;

        doNothing().when(scoreSpy.feeProvider).transferFund(token, value, randomAddr);

        // not owner
        Executable notOwnerCall = () -> score.invoke(notOwner, "transferFundFromFeeProvider", token, value, randomAddr);
        expectErrorMessage(notOwnerCall, "require owner access");

        // owner
        score.invoke(owner, "transferFundFromFeeProvider", token, value, randomAddr);
    }
}
