package org.smartregister.anc.library.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.anc.library.AncLibrary;
import org.smartregister.anc.library.activity.BaseUnitTest;
import org.smartregister.login.model.BaseLoginModel;
import org.smartregister.view.contract.BaseLoginContract;


/**
 * Created by ndegwamartin on 28/06/2018.
 */
@PrepareForTest(CoreLibrary.class)
public class LoginModelTest extends BaseUnitTest {

    private BaseLoginContract.Model model;
    public Context context;
    @Mock
    private CoreLibrary coreLibrary;

    @Before
    public void setUp() {
        model = new BaseLoginModel();
        context = Mockito.spy(Context.getInstance());
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", coreLibrary);

    }

    // This test should not be here is requires too much mocking of internal states and AndroidKeyStore
    // which is not available by default
    // It was transferred from the app module to the lib module
    // The fix might to add an actual OpenSRP application but again UserService testing should be in the
    // opensrp-client-core library which provides this data
    @Ignore
    @Test
    public void testIsUserLoggedOutShouldReturnTrue() {
        Assert.assertTrue(model.isUserLoggedOut());
    }

    @Test
    @Ignore
    //Already Tested somewhere else
    public void testGetOpenSRPContextShouldReturnValidValue() {

        Assert.assertNotNull(model.getOpenSRPContext());
    }

    @Test
    public void testIsPasswordValidShouldTrueWhenPasswordValidatesCorrectly() {
        boolean result = model.isPasswordValid(DUMMY_PASSWORD.toCharArray());
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPasswordValidShouldFalseWhenPasswordValidationFails() {
        boolean result = model.isPasswordValid("".toCharArray());
        Assert.assertFalse(result);
        result = model.isPasswordValid("A".toCharArray());
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEmptyUsernameShouldTrueWhenIsEmpty() {
        boolean result = model.isEmptyUsername("");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEmptyUsernameShouldFalseWhenUsernameIsNotEmpty() {
        boolean result = model.isEmptyUsername(DUMMY_USERNAME);
        Assert.assertFalse(result);
    }

}
