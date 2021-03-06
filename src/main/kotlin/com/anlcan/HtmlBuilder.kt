package com.anlcan

fun toHtml(table:List<Card>, players:List<Player>):String {
    var html = "<html><head>\n" +
            "    <meta htatp-equiv=\"refresh\" content=\"5\">"+
            "  </head><body>"
    html +="<table>"
    html +="<tr>"
    for (c in table){
        html +="<td><img src='src/main/resources/img/${toImage(c)}' width=\"50%\" height=\"50%\"/> </td>"
    }
    html +="</tr>"
    html +="</table>"

    for (p in players) {
        html +="<caption>${p.bestHand?.rank}</caption>"
        html +="<table>"
        html +="<tr>"
        for (c in p.cards()) {
            html += "<td> ${toImageTag(c)} </td>"
        }

        for (c in p.bestHand!!.cards) {
            html += "<td>${toImageTag(c)}</td>"
        }


        html += "</tr>"
        html +="</table>"
    }

    html +="</body>"

    return html
}
fun toImageTag(c: Card): String{
    return "<img src='src/main/resources/img/${toImage(c)}' width=\"30%\" height=\"30%\"/>"
}

fun toImage(c: Card): String {
    val suite = when(c.suit){
        Suit.Spades -> "S"
        Suit.Hearts -> "H"
        Suit.Diamonds -> "D"
        Suit.Clubs -> "C"
    }

    val value:String = when(c.value){
        Value.KING, Value.JACK, Value.QUEEN, Value.ACE -> c.value.toString()[0].toString()
        Value.TEN -> "0"
        else -> c.value.toString()
    }

    return "$value$suite.png"
}
