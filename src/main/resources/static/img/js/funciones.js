document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("form-busqueda");
    const nombreInput = document.getElementById("nombre");
    const categoriaInput = document.getElementById("categoria");

    // 🔁 Deshabilita uno al escribir en el otro
    nombreInput.addEventListener("input", function () {
        categoriaInput.disabled = nombreInput.value.trim().length > 0;
    });

    categoriaInput.addEventListener("input", function () {
        nombreInput.disabled = categoriaInput.value.trim().length > 0;
    });

    // 🛑 Por seguridad: evitar envío si ambos están llenos (por manipulación)
    form.addEventListener("submit", function (event) {
        const nombre = nombreInput.value.trim();
        const categoria = categoriaInput.value.trim();

        if (nombre && categoria) {
            event.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Oops...',
                text: 'Solo podés buscar por nombre o por categoría, no ambos.',
            });
        }
    });
});
