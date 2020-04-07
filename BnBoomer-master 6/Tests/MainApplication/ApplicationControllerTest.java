package MainApplication;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationControllerTest {
    ApplicationController appController = new ApplicationController();

    public ApplicationControllerTest() {
    }

    /**
     * Test if the data is null
     *
     * @throws Exception
     */
    @Test
    public void testIllegalData() throws Exception {
        System.out.println("testIllegalData executed");
        Assert.assertNotNull(this.appController.getAllListings());
    }

    /**
     * Test if filtered data is null
     *
     * @throws Exception
     */
    @Test
    public void testIllegalInitialFilteredListings() throws Exception {
        System.out.println("testIllegalInitialFilteredListings executed");
        Assert.assertNotNull(this.appController.getInitialFilteredListings());
    }

    /**
     * Test if all boroughs are in hashMap
     */
    @Test
    public void testHashMap_CorrectNumberOfKeys() {
        System.out.println("testHashMap_CorrectNumberOfKeys executed");
        appController.updateBoroughAbbreviation();
        Assert.assertEquals(33, appController.getBoroughAbbreviation().size());
    }

    /**
     * Test if the tab gets unlocked after prices are selected
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testIllegalSliderValuesAndCheckInOutDates() throws Exception {
        System.out.println("testIllegalSliderValuesAndCheckInOutDates executed");
        this.appController.setMinSlider(400.0D);
        this.appController.setMaxSlider(300.0D);
        boolean result = this.appController.unlockTabs();
        Assert.assertFalse(result);
    }
}
