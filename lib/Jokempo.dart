import 'dart:math';
import 'package:flutter/material.dart';

class Jokempo extends StatefulWidget {
  @override
  _JokempoState createState() => _JokempoState();

}

class _JokempoState extends State<Jokempo> {

  var _imagemApp = AssetImage("imagens/default.png");
  var _mensagem = "Choose An Option:";

   void _opcaoSelecionada(String escolhaUser){

    // print ("Opção selecionado:" + escolhaUser);
     var opcoes = ["pedra", "papel", "tesoura", "spock", "largato"];
     var numero = Random().nextInt(5);
     var escolhaApp = opcoes[numero];

     print ("Ecolha do App" + escolhaApp);
     print ("Escolha do User" + escolhaUser);

     // Exibir imagem escolhida pelo App
     switch( escolhaApp ){
       case "pedra" :
         setState(() {
           this._imagemApp = AssetImage("imagens/rock.png");
         });
         break;
       case "papel" :
         setState(() {
           this._imagemApp = AssetImage("imagens/paper.png");
         });
         break;
       case "tesoura" :
         setState(() {
           this._imagemApp = AssetImage("imagens/scissors.png");
         });
         break;
       case "spock" :
         setState(() {
           this._imagemApp = AssetImage("imagens/spock.png");
         });
         break;
       case "largato" :
         setState(() {
           this._imagemApp = AssetImage("imagens/lizard.png");
         });
         break;
     }

     //validar vencedor
     // Win
     if (escolhaUser == "pedra" && escolhaApp == "tesoura") {
       setState(() {
         this._mensagem = "You Won :) -  Rock crushes Scissors";
       });
     } else if (escolhaUser == "pedra" && escolhaApp == "largato") {
       setState(() {
         this._mensagem = "You Won :) -  Rock crushes Lizard";
       });
     } else if (escolhaUser == "papel" && escolhaApp == "pedra"){
       setState(() {
         this._mensagem = "You Won :) -  Paper covers Rock";
       });
     } else if (escolhaUser == "papel" && escolhaApp == "spock"){
       setState(() {
         this._mensagem = "You Won :) -  Paper disproves Spock";
       });
     } else if (escolhaUser == "tesoura" && escolhaApp == "papel"){
       setState(() {
         this._mensagem = "You Won :) -  Scissors cuts Paper";
       });
     } else if (escolhaUser == "tesoura" && escolhaApp == "largato"){
       setState(() {
         this._mensagem = "You Won :) -  Scissors decapitates Lizard";
       });
     } else if (escolhaUser == "spock" && escolhaApp == "tesoura"){
       setState(() {
         this._mensagem = "You Won :) -  Spock smashes Scissors";
       });
     } else if (escolhaUser == "spock" && escolhaApp == "pedra"){
       setState(() {
         this._mensagem = "You Won :) -  Spock vaporizes Rock";
       });
     } else if (escolhaUser == "largato" && escolhaApp == "papel"){
       setState(() {
         this._mensagem = "You Won :) -  Lizard eats Paper";
       });
     } else if(escolhaUser == "largato" && escolhaApp == "spock"){
       setState(() {
         this._mensagem = "You Won :) -  Lizard poisons Spock";
       });
     }
     // Lose
     else if (escolhaUser == "tesoura" && escolhaApp == "pedra") {
       setState(() {
         this._mensagem = "You Lost :( -  Rock crushes Scissors";
       });
     } else if (escolhaUser == "largato" && escolhaApp == "pedra") {
       setState(() {
         this._mensagem = "You Lost :( -  Rock crushes Lizard";
       });
     } else if (escolhaUser == "pedra" && escolhaApp == "papel"){
       setState(() {
         this._mensagem = "You Lost :( -  Paper covers Rock";
       });
     } else if (escolhaUser == "spock" && escolhaApp == "papel"){
       setState(() {
         this._mensagem = "You Lost :( -  Paper disproves Spock";
       });
     } else if (escolhaUser == "papel" && escolhaApp == "tesoura"){
       setState(() {
         this._mensagem = "You Lost :( -  Scissors cuts Paper";
       });
     } else if (escolhaUser == "largato" && escolhaApp == "tesoura"){
       setState(() {
         this._mensagem = "You Lost :( -  Scissors decapitates Lizard";
       });
     } else if (escolhaUser == "tesoura" && escolhaApp == "spock"){
       setState(() {
         this._mensagem = "You Lost :( -  Spock smashes Scissors";
       });
     } else if (escolhaUser == "pedra" && escolhaApp == "spock"){
       setState(() {
         this._mensagem = "You Lost :( -  Spock vaporizes Rock";
       });
     } else if (escolhaUser == "papel" && escolhaApp == "largato"){
       setState(() {
         this._mensagem = "You Lost :( -  Lizard eats Paper";
       });
     } else if(escolhaUser == "spock" && escolhaApp == "largato"){
       setState(() {
         this._mensagem = "You Lost :( -  Lizard poisons Spock";
       });

       // Escolhas Iguais

     } else {
       setState(() {
         this._mensagem = "Tied";
       });
     }


     {
     }


  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text ("Rock, Paper, Scissors, Lizard, Spock"),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Padding(
            padding: EdgeInsets.only(top: 32, bottom: 16),
            child: Text(
                "App Choice",
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold
             ),
            ),
          ),
          Image(image: this._imagemApp,),
          Padding(
            padding: EdgeInsets.only(top: 32, bottom: 16),
            child: Text(
              this._mensagem,
              textAlign: TextAlign.center,
              style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              GestureDetector(
                onTap: ()=> _opcaoSelecionada("pedra"),
                child: Image.asset("imagens/rock.png")
              ),
              GestureDetector(
                onTap:()=> _opcaoSelecionada("papel"),
                child: Image.asset("imagens/paper.png")
              ),
              GestureDetector(
                onTap:()=> _opcaoSelecionada("tesoura"),
                child: Image.asset("imagens/scissors.png")
              )
            ],
          ),
          Padding(
            padding: EdgeInsets.only(top: 32, bottom: 16),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              GestureDetector(
                onTap: ()=> _opcaoSelecionada("spock"),
                child: Image.asset("imagens/spock.png"),
              ),
              GestureDetector(
                onTap: ()=> _opcaoSelecionada("largato"),
                child: Image.asset("imagens/lizard.png")
              ),
            ],
          )
        ],
      ),
    );
  }
}
