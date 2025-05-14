function showPaymentModal() {
    const modal = new bootstrap.Modal(document.getElementById('confirmPaymentModal'));
    modal.show();
}

function showSuccessModalAndRedirect() {
    const modal = new bootstrap.Modal(document.getElementById('successPaymentModal'));
    modal.show();
    setTimeout(() => {
        window.location.href = "/";
    }, 3000); // след 3 секунди пренасочване
}

function showPaymentFailure() {
    alert("Недостатъчно средства. Покупката е прекратена.");
    window.location.href = "/";
}
