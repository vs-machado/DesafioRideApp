package com.phoenix.rideapp.core.helpers

class FakeLocationHelperImpl: LocationHelper {
    override fun getErrorEmptyOrigin() = "O endereço de origem não foi preenchido. Preencha o endereço e tente novamente."
    override fun getErrorEmptyDestination() = "O endereço de destino não foi preenchido. Preencha o endereço e tente novamente."
    override fun getErrorEmptyUserId() = "O ID de usuário não foi preenchido. Preencha o ID e tente novamente."
    override fun getErrorSameAddresses() = "Os endereços de destino de origem e destino não podem ser iguais. Preencha os endereços corretamente e tente novamente."
    override fun getErrorUnknown() = "Ocorreu um erro desconhecido."
    override fun getErrorRideConfirmationFailed() = "Falha ao confirmar a viagem."
    override fun getErrorRideRequestFailed() = "Falha ao solicitar a viagem."
    override fun getErrorNoInternet() = "Sem conexão com a internet. Tente novamente."
    override fun getErrorInvalidData() = "Dados inválidos. Corrija os dados e tente novamente."
    override fun getErrorServer() = "Um erro de servidor ocorreu. Tente novamente mais tarde."
    override fun getErrorContactSupport() = "Um erro desconhecido ocorreu. Contate a Central de Atendimento."
    override fun getErrorInvalidProvidedData() = "Os dados fornecidos são inválidos."
    override fun getErrorDriverSelectionFailed() = "Falha ao selecionar motorista. Selecione outro motorista disponível."
    override fun getErrorInvalidMileage() = "Quilometragem inválida para o motorista selecionado."
    override fun getErrorInvalidDriver() = "Motorista inválido. Corrija o nome e tente novamente."
    override fun getErrorNoRidesFound() = "Nenhuma corrida encontrada para o usuário e motorista selecionado."
    override fun getErrorUnexpected() = "Um erro inesperado ocorreu. Reinicie o aplicativo e tente novamente."
    override fun getErrorFetchHistory() = "Erro ao buscar histórico de corridas"
}