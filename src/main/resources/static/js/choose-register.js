function submitForm(id) {
    const form = document.getElementById('form-' + id);
    if (form) form.submit();
}

function alertClosedCashRegister() {
    alert("Касата е затворена и не може да бъде използвана.");
}