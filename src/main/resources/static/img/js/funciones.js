document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("form-busqueda");
    const nombreInput = document.getElementById("nombre");
    const categoriaInput = document.getElementById("categoria");

    form.addEventListener("submit", function (event) {
        const nombre = nombreInput.value.trim();
        const categoria = categoriaInput.value.trim();

        if (nombre && categoria) {
            event.preventDefault(); // Cancelar el envío
            Swal.fire({
                icon: 'warning',
                title: 'Oops...',
                text: 'Solo podés buscar por nombre o por categoría, no ambos.',
            });
        }
    });
});
