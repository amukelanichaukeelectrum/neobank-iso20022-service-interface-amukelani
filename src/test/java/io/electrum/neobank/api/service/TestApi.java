package io.electrum.neobank.api.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.electrum.neobank.api.model.Acmt007;
import io.electrum.neobank.api.model.Pacs002;
import io.electrum.neobank.api.model.Pacs003;
import io.electrum.neobank.api.model.Pacs004;
import io.electrum.neobank.api.model.Pacs008;

public class TestApi {

   @Test(description = "Tests that the constants defined in the service api are as expected")
   public void testServiceApiConstants() {
      Assert.assertEquals(ServiceApiConstants.API_BASE_PATH, "/neobank-Banking/iso20022-gateway/5.4.1");
   }

   @Test(description = "Tests that the constants defined in the api class are as expected")
   public void testApiResources() {
      Assert.assertEquals(AccountCacheMaintenanceApi.RESOURCE_PATH, "/v2/electronicPayments/accountCacheMaintenance");
      Assert.assertEquals(PaymentsApi.RESOURCE_PATH, "/v2/electronicPayments");
   }

   @Test(description = "Tests that the constants defined in in the SendCreditTransfer are as expected")
   public void testSendCreditTransfer() {
      Assert.assertEquals(PaymentsApi.SendCreditTransfer.OPERATION_NAME, "sendCreditTransfer");
      Assert.assertEquals(
            PaymentsApi.SendCreditTransfer.OPERATION_PATH,
            "/v2/electronicPayments" + "/fiTofiCustomerCreditTransfer");
      Assert.assertEquals(PaymentsApi.SendCreditTransfer.SUCCESS, 202);
   }

   @Test(description = "Tests that the constants defined in in the SendStatusReport are as expected")
   public void testSendStatusReport() {
      Assert.assertEquals(PaymentsApi.SendStatusReport.OPERATION_NAME, "sendStatusReport");
      Assert.assertEquals(
            PaymentsApi.SendStatusReport.OPERATION_PATH,
            "/v2/electronicPayments" + "/fiTofiPaymentStatusReport");
      Assert.assertEquals(PaymentsApi.SendStatusReport.SUCCESS, 202);
   }

   @Test(description = "Tests that the constants defined in in the CollectDirectDebit are as expected")
   public void testCollectDirectDebit() {
      Assert.assertEquals(PaymentsApi.CollectDirectDebit.OPERATION_NAME, "collectDirectDebit");
      Assert.assertEquals(
            PaymentsApi.CollectDirectDebit.OPERATION_PATH,
            "/v2/electronicPayments" + "/fiTofiCustomerDirectDebit");
      Assert.assertEquals(PaymentsApi.CollectDirectDebit.SUCCESS, 202);
   }

   @Test(description = "Tests that the constants defined in the PaymentReturnInitiation are as expected")
   public void testPaymentReturnInitiation() {
      Assert.assertEquals(PaymentsApi.PaymentReturnInitiation.OPERATION_NAME, "paymentReturnInitiation");
      Assert.assertEquals(
            PaymentsApi.PaymentReturnInitiation.OPERATION_PATH,
            "/v2/electronicPayments" + "/fiTofiPaymentReturn");
      Assert.assertEquals(PaymentsApi.PaymentReturnInitiation.SUCCESS, 202);
   }

   @Test(description = "Tests that the constants defined in in the UpdateCacheAccount are as expected")
   public void testUpdateCacheAccount() {
      Assert.assertEquals(AccountCacheMaintenanceApi.UpdateAccountCache.OPERATION_NAME, "updateAccountCache");
      Assert.assertEquals(
            AccountCacheMaintenanceApi.UpdateAccountCache.OPERATION_PATH,
            "/v2/electronicPayments" + "/accountCacheMaintenance");
      Assert.assertEquals(AccountCacheMaintenanceApi.UpdateAccountCache.SUCCESS, 204);
   }

   @Test(dataProvider = "resourcePaths", description = "Tests that the resource paths for the different API calls are "
         + "correct and are placed on the correct methods")
   public void testResourcePaths(Class<?> clazz, String methodName, String expectedPath, boolean exists) {
      List<String> methodPaths =
            Arrays.stream(clazz.getMethods())
                  .filter(method -> methodName.equals(method.getName()))
                  .map(method -> method.getAnnotation(Path.class))
                  .filter(Objects::nonNull)
                  .map(Path::value)
                  .collect(Collectors.toList());

      if (exists) {
         Assert.assertEquals(methodPaths.size(), 1);
         Assert.assertEquals(methodPaths.get(0), expectedPath);
      } else {
         Assert.assertEquals(methodPaths.size(), 0);
      }
   }

   @Test(dataProvider = "methodParams", description = "Tests that the there is only one function for each API call, i.e."
         + "there is no overloading. Furthermore, tests that each API call has the correct number and type of NonNull"
         + "parameters")
   public void testParams(Class<?> clazz, String methodName, List<Class<?>> expectedClasses) {
      List<Parameter[]> parametersList =
            Arrays.stream(clazz.getMethods())
                  .filter(method -> methodName.equals(method.getName()))
                  .map(Method::getParameters)
                  .collect(Collectors.toList());

      Assert.assertEquals(parametersList.size(), 1);
      Parameter[] parameters = parametersList.get(0);

      List<Class<?>> notNullParameters =
            Arrays.stream(parameters)
                  .filter(
                        parameter -> Arrays.stream(parameter.getAnnotations())
                              .anyMatch(annotation -> NotNull.class.equals(annotation.annotationType())))
                  .map(Parameter::getType)
                  .collect(Collectors.toList());

      Assert.assertEquals(notNullParameters, expectedClasses);
   }

   @Test(dataProvider = "headerParams", description = "Tests that the the expected headerParameters are specified in "
         + "API service methods")
   public void testHeaderParams(Class<?> clazz, String methodName, List<String> expectedHeaderParams) {
      List<Parameter[]> parametersList =
            Arrays.stream(clazz.getMethods())
                  .filter(method -> methodName.equals(method.getName()))
                  .map(Method::getParameters)
                  .collect(Collectors.toList());

      for (var params : parametersList) {
         var numHeaderParams =
               Arrays.stream(params)
                     .map(parameter -> Arrays.asList(parameter.getAnnotations()))
                     .flatMap(List::stream)
                     .filter(annotation -> HeaderParam.class.getName().equals(annotation.annotationType().getName()))
                     .collect(Collectors.toList())
                     .size();

         var headerParameters =
               Arrays.stream(params)
                     .map(parameter -> Arrays.asList(parameter.getAnnotations()))
                     .flatMap(List::stream)
                     .filter(annotation -> HeaderParam.class.getName().equals(annotation.annotationType().getName()))
                     .map(Annotation::toString)
                     .filter(string -> {
                        boolean found = false;
                        for (var expected : expectedHeaderParams) {
                           if (string.contains(expected)) {
                              found = true;
                              break;
                           }
                        }
                        return !found;
                     })
                     .collect(Collectors.toList());

         Assert.assertEquals(headerParameters.size(), 0);
         Assert.assertEquals(numHeaderParams, expectedHeaderParams.size());
      }
   }

   @Test(dataProvider = "queryParam", description = "Tests that the QueryParam and PathParam subclasses associated with each"
         + "API call has the correct number of fields for each subclass")
   public void testQueryParams(Class<?> clazz, int expectedFieldCount) {
      List<Field> fields = Arrays.stream(clazz.getFields()).collect(Collectors.toList());

      Assert.assertEquals(fields.size(), expectedFieldCount);
   }

   @Test(dataProvider = "requestType", description = "Tests that the request type associated with each API call is correct")
   public void testRequestTypes(Class<?> clazz, String methodName, Class<?> expectedRequestType) {
      List<Annotation[]> annotations =
            Arrays.stream(clazz.getMethods())
                  .filter(method -> methodName.equals(method.getName()))
                  .map(Method::getAnnotations)
                  .collect(Collectors.toList());

      Assert.assertEquals(annotations.size(), 1);

      for (Annotation annotation : annotations.get(0)) {
         if (annotation.annotationType().equals(expectedRequestType)) {
            return;
         }
      }

      Assert.fail();
   }

   @DataProvider
   public Object[][] resourcePaths() {
      return new Object[][] { { PaymentsApi.class, "sendCreditTransfer", "/fiTofiCustomerCreditTransfer", true },
            { PaymentsApi.class, "sendStatusReport", "/fiTofiPaymentStatusReport", true },
            { PaymentsApi.class, "collectDirectDebit", "/fiTofiCustomerDirectDebit", true },
            { PaymentsApi.class, "paymentReturnInitiation", "/fiTofiPaymentReturn", true },
            { AccountCacheMaintenanceApi.class, "updateAccountCache", "/accountCacheMaintenance", false }, };
   }

   @DataProvider
   public Object[][] methodParams() {
      return new Object[][] { { PaymentsApi.class, "sendCreditTransfer", List.of(Pacs008.class) },
            { PaymentsApi.class, "sendStatusReport", List.of(Pacs002.class) },
            { PaymentsApi.class, "collectDirectDebit", List.of(Pacs003.class) },
            { PaymentsApi.class, "paymentReturnInitiation", List.of(Pacs004.class) },
            { AccountCacheMaintenanceApi.class, "updateAccountCache", List.of(Acmt007.class) }, };
   }

   @DataProvider
   public Object[][] headerParams() {
      var listOfHeaders = List.of("X-Apikey", "x-party-key", "Actor", "Actor-Type");

      return new Object[][] { { PaymentsApi.class, "sendCreditTransfer", listOfHeaders },
            { PaymentsApi.class, "sendStatusReport", listOfHeaders },
            { PaymentsApi.class, "collectDirectDebit", listOfHeaders },
            { PaymentsApi.class, "paymentReturnInitiation", listOfHeaders },
            { AccountCacheMaintenanceApi.class, "updateAccountCache", listOfHeaders }, };
   }

   @DataProvider
   public Object[][] queryParam() {
      return new Object[][] { { PaymentsApi.SendCreditTransfer.QueryParameters.class, 0 },
            { PaymentsApi.SendCreditTransfer.PathParameters.class, 0 },
            { PaymentsApi.SendStatusReport.QueryParameters.class, 0 },
            { PaymentsApi.SendStatusReport.PathParameters.class, 0 },
            { PaymentsApi.CollectDirectDebit.QueryParameters.class, 0 },
            { PaymentsApi.CollectDirectDebit.PathParameters.class, 0 },
            { PaymentsApi.PaymentReturnInitiation.QueryParameters.class, 0 },
            { PaymentsApi.PaymentReturnInitiation.PathParameters.class, 0 },
            { AccountCacheMaintenanceApi.UpdateAccountCache.QueryParameters.class, 0 },
            { AccountCacheMaintenanceApi.UpdateAccountCache.PathParameters.class, 0 }, };
   }

   @DataProvider
   public Object[][] requestType() {
      return new Object[][] { { PaymentsApi.class, "sendCreditTransfer", POST.class },
            { PaymentsApi.class, "sendStatusReport", POST.class },
            { PaymentsApi.class, "collectDirectDebit", POST.class },
            { PaymentsApi.class, "paymentReturnInitiation", POST.class },
            { AccountCacheMaintenanceApi.class, "updateAccountCache", PUT.class }, };
   }

}
