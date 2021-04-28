package com.robertruzsa.codereader.presentation.screens

import com.internal.icell.basearch.viewmodel.BaseViewModel
import com.robertruzsa.codereader.model.CodeReaderAction
import com.robertruzsa.codereader.model.CodeReaderUIModel

class CodeReaderViewModel : BaseViewModel<CodeReaderUIModel, CodeReaderAction>(::CodeReaderUIModel) {

    override fun handleActions(action: CodeReaderAction) {
        TODO("Not yet implemented")
    }

}
