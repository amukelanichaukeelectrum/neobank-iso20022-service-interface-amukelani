package io.electrum.neobank.api.model;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.electrum.neobank.api.model.utils.ApiModelValidator;
import io.electrum.vas.JsonUtil;

/**
 * Test basic creation of the new models.
 */
public class NewModelTest {
   private ObjectMapper mapper;

   @BeforeClass
   public void beforeClass() {
      mapper = JsonUtil.getBaseMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   }

   @Test(description = "Test that we can serialise each model to the expected value", dataProvider = "modelDataProvider")
   public <T> void testCreateModel(String filePath, Class<T> clazz) throws Exception {

      Object model1 = mapper.readValue(JsonUtil.readFileAsString(filePath, false), clazz);
      T model2 = mapper.readValue(JsonUtil.serialize(model1), clazz);

      ApiModelValidator.validate(model1);
      ApiModelValidator.validate(model2);

      Assert.assertEquals(model1, model2);
      Assert.assertEquals(model1.hashCode(), model2.hashCode());
      Assert.assertEquals(model1.toString(), model2.toString());
      Assert.assertNotEquals(model1, new Object());
      Assert.assertEquals(JsonUtil.serialize(model1), JsonUtil.serialize(model2));
   }

   @DataProvider(name = "modelDataProvider")
   public Object[][] modelDataProvider() {
      return new Object[][] {
              { "payment_status_report.json", Pacs002.class },
              { "fitofi_credit_transfer.json", Pacs008.class },
              { "group_header_11.json", GroupHeader101.class },
              { "application_header.json", BusinessApplicationHeaderV03.class },
              { "original_group_head_17.json", OriginalGroupHeader17.class },
              { "payment_transaction_130.json", PaymentTransaction130.class },
              { "original_transaction_reference_35.json", OriginalTransactionReference35.class },
              { "group_header_96.json", GroupHeader96.class },
              { "credit_transfer_transaction_50_inner.json", CreditTransferTransaction50Inner.class },
              { "mandate_related_data_2_choice.json", MandateRelatedData2Choice.class },
              { "account_mirror_request.json", Acmt007.class },
              { "fitofi_direct_debit.json", Pacs003.class },
              { "fitofi_payment_return_dd.json", Pacs004.class },
              { "fitofi_payment_return_ct.json", Pacs004.class }};
   }

}
