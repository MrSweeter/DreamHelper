# DreamHelper

[Spigot](https://www.spigotmc.org/resources/dreamhelper-1-9-1-12-support-db.39072/)

_EDIT: Post rewriten for 2.0-beta_

![](http://i.imgur.com/eP35EEz.png)

You need a "player" capable of answering certain questions at any time ? You want to allow your staff to answer the request of any players, any time ?

Then this plugin maybe going to please you. Indeed, it allows you to have a configurable « bot » but also a system of ticket.

Like that, the players can make a demand (/dhticket <demand-message>) anytime and yout staff can take care of it as soon as possible

Tested with database : PhpMyAdmin

![](http://i.imgur.com/9XnrMq2.png)

**configuration.yml**

![](http://i.imgur.com/7pQw5qW.png)

**- color :**  String you will put before the color code like (&6 = §6)

**- purge-timeDay-expiration :**  When you use /dhpurgeticket -a/-c, if you don’t put -a or -c, the command will remove all old message in function of the value here (in day).

**-** **conversation-mode :**  For the bot, would you like all players can see question and answer or only the player who ask ?

**conversation-mode : true**  (all players see this)

![](http://i.imgur.com/dlMFB5C.png)

**conversation-mode : false**

![](http://i.imgur.com/eGtQg6z.png)

**- bot-name :** if you put  **conversation-mode to true**, the bot must have a name no ?

**- check-all-message :** When a player send message in chat, will the plugin have to try to answer to every message or only those who have « ? » ?

**- ticket-prefix :**  For ticket, you can specified some prefix

![](http://i.imgur.com/rsMnGlc.png)

**- help-staff :**  For a player who have  **dreamhelper.ticket.help**, what will he see ?

![](http://i.imgur.com/IBqU1qf.png)

**- storage :**

- **- enable :**  Enable database or use  **ticket.yml** to manage the tickets ? (if you enable DB, the table will be created on start of plugin)

<details>
	<summary>ticket.yml</summary>
  
![](http://i.imgur.com/tTTr7Ab.png) 
  
</details>

- **- url :** url to use for connection

- **- host:** host to use for connection

- **- database:**  name of the database where tou want to create the table of ticket

- **- user:** user to use for connection

- **- password:** password to use for connection

**faq.yml**

![](http://i.imgur.com/Silztrv.png)

List all questions which will have the answer the bot, keep structure like

(open plugin like a .zip or a .rar, and you will find an example of faq.yml)

    Identifier:
	    Keywords: ''
	    Questions: '
	    Answer: ''

**lang-message.yml**

![](http://i.imgur.com/J5OtH14.png)

Custom your own messages

![](http://i.imgur.com/z6GjoYm.png)

**For any player :**

-   **/dhticket :**  Open a ticket for the staff (/dhticket <your request>)

**For staff player :**

-   **/dhtickets :**  Show all open tickets (/dhtickets [-a][-c])

	- Default : only opened (submit and assigned) tickets 
	- **-a**  **:** All tickets 
	- **-c :**  Only closed tickets

-   **/dhcheckticket :**  Show you a ticket without change his status (/dhcheckticket <number of ticket>)

-   **/dhtaketicket :** Teleport you to ticket, and show it (/dhtaketicket <number of ticket>)

-   **/dhreplyticket :** Answer to a ticket without closing it (/dhreplyticket <number of ticket> <answer to message>)

-   **/dhcloseticket :** Close a ticket (/dhcloseticket <number of ticket>)

-   **/dhpurgeticket :**  Remove ticket in database or in tickets.yml (/dhpurgeticket [-a][-c])
	-  Default : only timed tickets (see purge-timeDay-expiration in  configuration.yml)
	- **-a :**  All tickets
	- **-c :** Only closed tickets

-   **/dhdeleteticket :**  Remove a ticket in database or in tickets.yml (/dhdeleteticket <number of ticket>)

-   **/dhthelp :**  Show you the help configure in configuration.yml (you can use it for all player also)

**For Administrator :**

-   **/dhreload :**  Reload all configurations files

![](http://i.imgur.com/gI1IprT.png)

-   **/dhticket :  dreamhelper.ticket.send**

-   **/dhtickets :  dreamhelper.ticket.seen**

-   **/dhcheckticket :  dreamhelper.ticket.check**

-   **/dhtaketicket :  dreamhelper.ticket.take**

-   **/dhreplyticket :  dreamhelper.ticket.reply**

-   **/dhcloseticket :  dreamhelper.ticket.close**

-   **/dhpurgeticket :  dreamhelper.ticket.purge**

-   **/dhdeleteticket :  dreamhelper.ticket.delete**

-   **/dhthelp :  dreamhelper.ticket.help**

-   **/dhreload :  dreamhelper.reload**

![](http://i.imgur.com/r7oH8Tg.png)

![](http://i.imgur.com/XWDE5U7.png)

![](http://i.imgur.com/Zc7hI3G.png)

<details>
<summary>More Screens ?</summary>

![](http://i.imgur.com/Xqz727U.png)

![](http://i.imgur.com/x8fOKNG.png)  ![](http://i.imgur.com/5looaBD.png)  ![](http://i.imgur.com/5DgLHE3.png)  ![](http://i.imgur.com/RuCDiSf.png)  ![](http://i.imgur.com/9zLO6Tl.png)  ![](http://i.imgur.com/Dxwf4Iy.png)  ![](http://i.imgur.com/96nQsNS.png)  ![](http://i.imgur.com/SAKiC4F.png)  ![](http://i.imgur.com/hNHZz5f.png)  ![](http://i.imgur.com/o0F3xnw.png)  ![](http://i.imgur.com/1znOuHc.png)  ![](http://i.imgur.com/84wR9Vg.png)  ![](http://i.imgur.com/lc0ns5v.png)
</details>
