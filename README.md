# neobank-iso20022-service-interface

## 

The service interface for neobank-ISO20022

## Building

neobank use `OneOf:` in a way that we do not full support in their OpenAPI document. If you are updating the OpenAPI (in the case of having received a new version from neobank), please make use of the `SwaggerPreprocess2.py` file to provide a supported version of their OpenAPI doc.

Follow these steps:
1. Add new swagger file
2. Run the `SwaggerPreprocess2.py` script on the new swagger file
3. This will output an `output.swagger.yaml` file that contains a supported implementation of `OneOf:`
4. Copy the contents of `output.swagger.yaml` and paste it into the `iso20022.swagger.yaml` file

## Maven

Example dependency definition from the
[neobank gateway message library](https://github.com/electrumpayments/gateway-message-neobank/):

```
<properties>
   <neobank-iso20022-service-interface-version>{current pom.xml version}</neobank-iso20022-service-interface-version>
</properties>
<dependencies>
   <dependency>
     <groupId>io.electrum</groupId>
     <artifactId>neobank-iso20022-service-interface</artifactId>
     <version>${neobank-iso20022-service-interface-version}</version>
     <scope>compile</scope>
   </dependency>
   ...
</dependencies>
```