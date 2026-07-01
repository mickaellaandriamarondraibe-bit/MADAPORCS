<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Mouvements du lot</h1>
    <p>Effectif total: ${effectifTotal}</p>
    <ul>
       <c:forEach items="${mouvements}" var="mouvement">
            <li>${mouvement.description}</li>
        </c:forEach>
    </ul>

    <div>
        <a href="/lots/${id}/mouvements/new">Ajouter un mouvement</a>
    </div>
</body>
</html>