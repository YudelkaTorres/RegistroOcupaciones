package edu.ucne.registroocupaciones.domain.empleados.usecase

import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository,
){
    suspend operator fun invoke(empleado: Empleado): Result<Int> {

        val fecha = EmpleadoValidations.validateFechaIngreso(empleado.fechaIngreso)
        if (!fecha.isValid) {
            return Result.failure(IllegalArgumentException(fecha.error))
        }

        val nombres = EmpleadoValidations.validateNombres(empleado.nombres)
        if (!nombres.isValid) {
            return Result.failure(IllegalArgumentException(nombres.error))
        }

        val sexo = EmpleadoValidations.validateSexo(empleado.sexo)
        if (!sexo.isValid) {
            return Result.failure(IllegalArgumentException(sexo.error))
        }

        val sueldo = EmpleadoValidations.validateSueldo(empleado.sueldo)
        if (!sueldo.isValid) {
            return Result.failure(IllegalArgumentException(sueldo.error))
        }

        return runCatching {
            repository.upsert(empleado)
        }
    }
}