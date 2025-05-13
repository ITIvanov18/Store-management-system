document.addEventListener("DOMContentLoaded", function () {
    const categorySelect = document.querySelector('select[name="category"]');
    const expirationCheck = document.querySelector('input[name="hasExpirationDate"]');
    const expirationDateInput = document.querySelector('input[name="expirationDate"]');

    function updateExpirationFields() {
        const selected = categorySelect.value;

        if (selected === 'NON_FOOD') {
            expirationCheck.checked = false;
            expirationCheck.disabled = true;
            expirationDateInput.disabled = true;
            expirationDateInput.value = '';
        } else {
            expirationCheck.disabled = false;
            expirationDateInput.disabled = !expirationCheck.checked;
        }
    }

    // При промяна на категория
    categorySelect.addEventListener("change", updateExpirationFields);

    // При кликане на чекбокса
    expirationCheck.addEventListener("change", () => {
        expirationDateInput.disabled = !expirationCheck.checked;
    });

    updateExpirationFields();
});
