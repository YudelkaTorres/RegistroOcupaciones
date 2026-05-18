package edu.ucne.registroocupaciones.presentation.empleados.edit

data class EmpleadoEditUiState(
    val empleadoId: Int? = null,
    val fechaIngreso: String = "",
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: String = "",
    val fechaIngresoError: String? = null,
    val nombresError: String? = null,
    val sexoError: String? = null,
    val sueldoError: String? = null,
    val showDatePicker: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)
