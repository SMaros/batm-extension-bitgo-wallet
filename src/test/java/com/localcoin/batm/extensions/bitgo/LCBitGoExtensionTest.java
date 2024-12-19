package com.localcoin.batm.extensions.bitgo;

import com.generalbytes.batm.server.extensions.IExtensionContext;
import com.localcoin.batm.extension.bitgo.LCBitGoExtension;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LCBitGoExtensionTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty(BitgoWalletTest.BATM_CONFIG_PATH, "classpath:");
    }

    @Test
    public void testLCFireblocksExtensionCtx() {
        LCBitGoExtension extension = new LCBitGoExtension();
        IExtensionContext context = mock(IExtensionContext.class);
        extension.init(context);
        assertEquals(context, extension.getCtx());
    }
}
