document.addEventListener("DOMContentLoaded", function () {
    const hasError = document.getElementById("stockErrorModal");
    if (hasError) {
        const modal = new bootstrap.Modal(hasError);
        modal.show();
    }
});
