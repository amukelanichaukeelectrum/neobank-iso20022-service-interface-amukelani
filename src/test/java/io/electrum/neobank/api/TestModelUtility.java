package io.electrum.neobank.api;

import java.util.UUID;

import io.electrum.neobank.api.model.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.electrum.vas.JsonUtil;

public class TestModelUtility {

   @Test
   public void testGetMsgIdAndUUIDPacs002() throws Exception {
      Pacs002 pacs002 = JsonUtil.deserialiseJsonObjectFromFile("payment_status_report.json", Pacs002.class, false);

      Assert.assertEquals(ModelUtility.getUetr(pacs002).get(), UUID.fromString("319aef79-1f2b-4079-a95f-c4f4fbe5dedb"));
      Assert.assertEquals(ModelUtility.getMsgId(pacs002), "pacs002messageId23");
   }


   @Test
   public void testGetMsgIdAndUUIDPacs004CreditTransfer() throws Exception {
      Pacs004 pacs004 = JsonUtil.deserialiseJsonObjectFromFile("fitofi_payment_return_ct.json", Pacs004.class, false);

      Assert.assertEquals(ModelUtility.getUetr(pacs004).get(), UUID.fromString("319aef79-1f2b-4079-a95f-c4f4fbe5dedb"));
      Assert.assertEquals(ModelUtility.getMsgId(pacs004), "unique-message-id");
   }

   @Test
   public void testGetMsgIdAndUUIDPacs004DirectDebit() throws Exception {
      Pacs004 pacs004 = JsonUtil.deserialiseJsonObjectFromFile("fitofi_payment_return_dd.json", Pacs004.class, false);

      Assert.assertEquals(ModelUtility.getUetr(pacs004).get(), UUID.fromString("319aef79-1f2b-4079-a95f-c4f4fbe5dedb"));
      Assert.assertEquals(ModelUtility.getMsgId(pacs004), "unique-message-id");

   }
}
