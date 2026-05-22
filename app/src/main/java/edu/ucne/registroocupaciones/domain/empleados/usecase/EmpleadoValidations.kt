package edu.ucne.registroocupaciones.domain.empleados.usecase

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

object EmpleadoValidations {

    fun validateFechaIngreso(value: String): ValidationResult {
        return if (value.isBlank())
            ValidationResult(false, "Debe seleccionar la fecha")
        else
            ValidationResult(true)
    }

    fun validateNombres(value: String): ValidationResult {
        return if (value.isBlank())
            ValidationResult(false, "Los nombres son obligatorios")
        else if (value.length < 3)
            ValidationResult(false, "El nombre es muy corto")
        else
            ValidationResult(true)
    }

    fun validateSexo(value: String): ValidationResult {
        return if (value.isBlank())
            ValidationResult(false, "Debe seleccionar el sexo")
        else
            ValidationResult(true)
    }

    fun validateSueldo(value: Double?): ValidationResult {
        return if (value == null || value <= 0.0)
            ValidationResult(false, "El sueldo debe ser mayor que 0")
        else
            ValidationResult(true)
    }
}
