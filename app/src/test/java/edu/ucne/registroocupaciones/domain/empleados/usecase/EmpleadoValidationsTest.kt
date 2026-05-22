package edu.ucne.registroocupaciones.domain.empleados.usecase

import org.junit.Assert.*
import org.junit.Test

class EmpleadoValidationsTest {
    @Test
    fun `fecha vacia retorna error`() {
        val result = EmpleadoValidations.validateFechaIngreso("")

        assertFalse(result.isValid)
        assertEquals("Debe seleccionar la fecha", result.error)
    }

    @Test
    fun `nombre corto retorna error`() {
        val result = EmpleadoValidations.validateNombres("Jo")

        assertFalse(result.isValid)
    }

    @Test
    fun `sexo vacio retorna error`() {
        val result = EmpleadoValidations.validateSexo("")

        assertFalse(result.isValid)
    }

    @Test
    fun `sueldo menor o igual a cero retorna error`() {
        val result = EmpleadoValidations.validateSueldo(0.0)

        assertFalse(result.isValid)
    }

    @Test
    fun `datos validos retornan true`() {
        val result = EmpleadoValidations.validateNombres("Juan Perez")

        assertTrue(result.isValid)
    }
}