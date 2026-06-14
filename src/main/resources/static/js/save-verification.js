async function validateMaleInput(lotPorcId) {
    try {
        const url = `/api/lots/mouvements/${lotPorcId}`;
        const response = await fetch(url);
        const lotPorc = await response.text();

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        if (lotPorc) {
            const lotPorcData = JSON.parse(lotPorc);
            validateMaleQuantity(lotPorcData);
        } else {
            console.error("No data received for the specified lotPorcId.");
        }
    } catch (error) {
        console.error("Error during form validation:", error);
    }
}

async function validateFemaleInput(lotPorcId) {
    try {
        const url = `/api/lots/mouvements/${lotPorcId}`;
        const response = await fetch(url);
        const lotPorc = await response.text();

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        if (lotPorc) {
            const lotPorcData = JSON.parse(lotPorc);
            validateFemaleQuantity(lotPorcData);
        } else {
            console.error("No data received for the specified lotPorcId.");
        }
    } catch (error) {
        console.error("Error during form validation:", error);
    }
}

function validateMaleQuantity(lotPorc) {
    const qttMale = document.getElementById("qtt_male").value;
    const maleError = document.getElementById("qtt_male_error");

    const typeMouvement = document.getElementById("type_mouvement").value;
    const btn = document.getElementById("save-btn");

    if ( parseInt(qttMale) < 0) {
        maleError.textContent = "La quantité des mâles ne peut pas être négative.";
        btn.disabled = true;
    } else {
        maleError.textContent = "";
    }

    if ( typeMouvement == "3" || typeMouvement == "4") {
        if ( parseInt(qttMale) > `${lotPorc.quantiteMale}` ) {
            maleError.textContent = "La quantité des mâles ne peut pas dépasser la quantité disponible.";
            btn.disabled = true;
        } else {
            maleError.textContent = "";
        }

    }
}   

function validateFemaleQuantity(lotPorc) {
    const qttFemelle = document.getElementById("qtt_femelle").value;
    const femelleError = document.getElementById("qtt_femelle_error");

    const typeMouvement = document.getElementById("type_mouvement").value;
    const btn = document.getElementById("save-btn");

    if ( parseInt(qttFemelle) < 0) {
        femelleError.textContent = "La quantité des femelles ne peut pas être négative.";
        btn.disabled = true;
    } else {
        femelleError.textContent = "";
    }

    if ( typeMouvement == "3" || typeMouvement == "4") {
        if ( parseInt(qttFemelle) > `${lotPorc.quantiteFemelle}` ) {
            femelleError.textContent = "La quantité des femelles ne peut pas dépasser la quantité disponible.";
            btn.disabled = true;
        } else {
            femelleError.textContent = "";
        }

    }
}   

document.addEventListener("DOMContentLoaded", function() {
    const qttMaleInput = document.getElementById("qtt_male");
    const qttFemelleInput = document.getElementById("qtt_femelle");

    qttMaleInput.addEventListener("blur", function() {
        const lotPorcId = document.getElementById("lotPorcId").value;
        await validateMaleInput(lotPorcId);
    });

    qttFemelleInput.addEventListener("blur", function() {
        const lotPorcId = document.getElementById("lotPorcId").value;
        await validateFemaleInput(lotPorcId);
    });
});