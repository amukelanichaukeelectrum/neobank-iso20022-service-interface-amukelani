package io.electrum.neobank.api;

import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.electrum.neobank.api.model.*;

public class ModelUtility {


   public static String getMsgId(@NotNull Pacs002 pacs002) {
      return Optional.ofNullable(pacs002)
            .map(Pacs002::getFiToFiPaymentStatusReport)
            .map(FIToFIPaymentStatusReportV12::getGroupHeader)
            .map(GroupHeader101::getMessageIdentification)
            .orElse(null);
   }


   public static String getMsgId(@NotNull Pacs004 pacs004) {
      return Optional.ofNullable(pacs004)
            .map(Pacs004::getPaymentReturn)
            .map(PaymentReturnV09::getGroupHeader)
            .map(GroupHeader90::getMessageIdentification)
            .orElse(null);
   }


   public static Optional<UUID> getUetr(Pacs002 pacs002) {
      return Optional.ofNullable(pacs002)
            .map(Pacs002::getFiToFiPaymentStatusReport)
            .map(FIToFIPaymentStatusReportV12::getTransactionInformationAndStatus)
            .map(list -> list.get(0))
            .map(PaymentTransaction130::getOriginalUETR)
            .map(UUID::fromString);
   }


   public static Optional<UUID> getUetr(Pacs004 pacs004) {
      return Optional.ofNullable(pacs004)
            .map(Pacs004::getPaymentReturn)
            .map(PaymentReturnV09::getTransactionInformation)
            .map(list -> list.get(0))
            .map(PaymentTransaction112::getOriginalUETR)
            .map(UUID::fromString);
   }

   public static String getMsgId(@NotNull Pacs008 pacs008){
      return Optional.ofNullable(pacs008)
              .map(Pacs008::getFiToFiCustomerCreditTransfer)
              .map(FIToFICustomerCreditTransferV10::getGroupHeader)
              .map(GroupHeader96::getMessageIdentification)
              .orElse(null);
   }
   public static String getMsgId(@NotNull Pacs003 pacs003){
      return Optional.ofNullable(pacs003)
              .map(Pacs003::getFiToFiCustomerDirectDebit)
              .map(FIToFICustomerDirectDebitV08::getGroupHeader)
              .map(GroupHeader96::getMessageIdentification)
              .orElse(null);
   }
  public static String getMsgId(@NotNull Acmt007 acmt007){
     return Optional.ofNullable(acmt007)
             .map(Acmt007::getAccountOpeningRequest)
             .map(AccountOpeningRequestV04::getReferences)
             .map(References4::getMessageIdentification)
             .map(MessageIdentification1::getIdentification)
             .orElse(null);
  }

   public static Optional<UUID> getUetr(Pacs003 pacs003){
      return Optional.ofNullable(pacs003)
              .map(Pacs003::getFiToFiCustomerDirectDebit)
              .map(FIToFICustomerDirectDebitV08:: getDirectDebitTransactionInformation)
              .map(list -> list.get(0))
              .map(DirectDebitTransactionInformation24Inner::getPaymentIdentification)
              .map(PaymentIdentification13::getUETR)
              .map(UUID::fromString);
   }

   public static Optional<UUID> getUetr(Pacs008 pacs008){
      return Optional.ofNullable(pacs008)
              .map(Pacs008::getFiToFiCustomerCreditTransfer)
              .map(FIToFICustomerCreditTransferV10:: getCreditTransferTransactionInformation)
              .map(list -> list.get(0))
              .map(CreditTransferTransaction50Inner::getPaymentIdentification)
              .map(PaymentIdentification13::getUETR)
              .map(UUID::fromString);
   }
}
