package io.electrum.neobank.api;

import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.electrum.neobank.api.model.AccountOpeningRequestV04;
import io.electrum.neobank.api.model.Acmt007;
import io.electrum.neobank.api.model.CreditTransferTransaction50Inner;
import io.electrum.neobank.api.model.DirectDebitTransactionInformation24Inner;
import io.electrum.neobank.api.model.FIToFICustomerCreditTransferV10;
import io.electrum.neobank.api.model.FIToFICustomerDirectDebitV08;
import io.electrum.neobank.api.model.FIToFIPaymentStatusReportV12;
import io.electrum.neobank.api.model.GroupHeader101;
import io.electrum.neobank.api.model.GroupHeader90;
import io.electrum.neobank.api.model.GroupHeader96;
import io.electrum.neobank.api.model.MessageIdentification1;
import io.electrum.neobank.api.model.Pacs002;
import io.electrum.neobank.api.model.Pacs003;
import io.electrum.neobank.api.model.Pacs004;
import io.electrum.neobank.api.model.Pacs008;
import io.electrum.neobank.api.model.PaymentIdentification13;
import io.electrum.neobank.api.model.PaymentReturnV09;
import io.electrum.neobank.api.model.PaymentTransaction112;
import io.electrum.neobank.api.model.PaymentTransaction130;
import io.electrum.neobank.api.model.References4;

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

   //TODO
//   public static String getMsgId(@NotNull Pacs008 pacs008)
//
//   public static String getMsgId(@NotNull Pacs003 pacs003)
//
//   public static String getMsgId(@NotNull Acmt007 acmt007)
//
//   public static Optional<UUID> getUetr(Pacs003 pacs003)
//
//   public static Optional<UUID> getUetr(Pacs008 pacs008)
}
