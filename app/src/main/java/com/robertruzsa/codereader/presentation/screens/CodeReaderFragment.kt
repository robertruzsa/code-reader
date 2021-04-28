package com.robertruzsa.codereader.presentation.screens

import androidx.fragment.app.viewModels
import com.internal.icell.basearch.fragment.BaseFragment
import com.internal.icell.basearch.utils.viewBinding
import com.robertruzsa.codereader.databinding.FragmentCodeReaderBinding
import com.robertruzsa.codereader.model.CodeReaderAction
import com.robertruzsa.codereader.model.CodeReaderUIModel

class CodeReaderFragment : BaseFragment<CodeReaderUIModel, CodeReaderAction>() {

    override val binding by viewBinding(FragmentCodeReaderBinding::inflate)

    override val viewModel: CodeReaderViewModel by viewModels()

}