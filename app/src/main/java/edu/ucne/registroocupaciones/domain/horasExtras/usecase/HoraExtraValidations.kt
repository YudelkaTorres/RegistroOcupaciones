package edu.ucne.registroocupaciones.domain.horasExtras.usecase

import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateEmpleado(
    empleadoId: Int?
): ValidationResult {

    return when {

        empleadoId == null || empleadoId <= 0 ->
            ValidationResult(
                false,
                "Debe seleccionar un empleado"
            )

        else ->
            ValidationResult(true)
    }
}

fun validateTipoHoraExtra(
    tipoHoraExtra: TipoHoraExtra?
): ValidationResult {

    return if (tipoHoraExtra == null) {
        ValidationResult(
            false,
            "Debe seleccionar el tipo de hora extra"
        )
    } else {
        ValidationResult(true)
    }
}

fun validateHorasTrabajadas(
    horas: String
): ValidationResult {

    return when {

        horas.isBlank() ->
            ValidationResult(
                false,
                "Las horas trabajadas son requeridas"
            )

        horas.toDoubleOrNull() == null ->
            ValidationResult(
                false,
                "Debe ingresar un número válido"
            )

        horas.toDouble() <= 0 ->
            ValidationResult(
                false,
                "Las horas deben ser mayores que 0"
            )

        else ->
            ValidationResult(true)
    }
}

fun validateHorasNocturnas(
    horasNocturnas: String,
    horasTrabajadas: String
): ValidationResult {

    val nocturnas =
        horasNocturnas.toDoubleOrNull() ?: 0.0

    val trabajadas =
        horasTrabajadas.toDoubleOrNull() ?: 0.0

    return when {

        horasNocturnas.isBlank() ->
            ValidationResult(true)

        nocturnas < 0 ->
            ValidationResult(
                false,
                "Las horas nocturnas no pueden ser negativas"
            )

        nocturnas > trabajadas ->
            ValidationResult(
                false,
                "Las horas nocturnas no pueden exceder las trabajadas"
            )

        else ->
            ValidationResult(true)
    }
}