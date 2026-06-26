<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Analyse du lot</title>
</head>
<body>

    <h1>Analyse du lot : ${lot.codeLot}</h1>
    
    <h2>Informations</h2>
    <p>Total femelles : ${analyse.nbFemellesSailliesTotal}</p>
    <p>En cycle : ${analyse.nbEnCycle}</p>
    <p>À surveiller : ${analyse.nbASurveiller}</p>
    <p>À retirer reproduction : ${analyse.nbARetirerReproduction}</p>
    
    <h2>Indicateurs</h2>
    <p>Taux d'aptitude global : ${tauxAptitudeGlobal} %</p>
    <p>Taux recommandé : ${tauxRecommande} %</p>
    <p>Taux de fertilité : ${tauxFertilite} %</p>
    
    <h2>Décision</h2>
    <p>${decision}</p>

</body>
</html>