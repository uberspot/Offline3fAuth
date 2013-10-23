#Offline 3 Factor Authentication (O3FA)

###Project aim

The current project integrates a multi-factor authentication system in a mobile platform. The aim of the project is to introduce an authentication framework that will not use a database in it's approach and thus it won't be vulnerable to all network related attacks that exploit the vulnerabilities of a remote database. 

###Proposed solution

In traditional authentication methods the confirmation of someones' identity is done through the comparison of the credentials provided with the credentials stored in a database (access list). In this approach the authentication will be done through the decryption of a physical asset (QR-code) that will be compared with the inputs of the system at that time (face recognition pattern and password). That way there is no need for a database to be held and the system becomes immune to network based attacks and to alteration of the credentials stored in them.

This project is the implementation part of Antonis Mougiakakos Msc Thesis.

###Specific parts

 1. Face recognition: The current project deals with authentication method with no database reference. To achieve this is uses the Local Binary Pattern algorithm as it is proven to be the most efficient face recognition algorithm that works with no database reference.
 2. QR code technology: Since the authentication scheme has to be stored in a way that will consist a physical asset a QR code is considered a cheap and efficient solution and has the advantage of being either volatile (if printed e.g. on paper) or persistent if printed on plastic cards and/or if kept on a smartphone. It is also easy to be carried by the user and can be read even by the majority of everyday mobile devices making it portable and flexible in usage. Lastly the QR code compared to other 2D barcodes is able to store a larger volume of information. 
 3. Encryption technologies: The data stored in the authentication asset need to be secure, so that no attacker will be able to reproduce them. Moreover the size of the information that is going to be stored in the QR code has to be restricted to QR standard byte size limits. That is why 3DES is preferred, as it is a very safe encryption method and also keeps the size of the output almost in the same level as the original data. The key that will be used for the encryption is the password input set by the user and the same password will be necessary for the decryption of the datagram and the authentication of the user.

###Flow Diagram

The flow is as follows

To (de)compose the encrypted QR code you do getFaceDescriptor <-> compress with zip algorithm <-> encrypt with 3DES and user password <-> encode to Base64 with ISO-8859-1 encoding so that it can be stored as a string in the qrcode

![flowDiagram](https://raw.github.com/uberspot/Offline3FAuth/master/flow.png "Flow Diagram")

###Libraries used

 1. Commons Codec for Base64 encoding/decoding
 2. Google-gson for json encoding/decoding
 3. ZXing for QRCode reading/writing
 4. LocalBinaryPattern algorithm from android-eye project for face detection/recognition

##License

    This project is licensed under the Apache version 2.0 license. See LICENSE file in the project folder